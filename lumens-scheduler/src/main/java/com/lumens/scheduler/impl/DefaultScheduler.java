/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformEngine;
import com.lumens.scheduler.JobScheduler;
import com.lumens.scheduler.JobTrigger;
import com.lumens.scheduler.JobMonitor;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.dao.RelationDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.entity.Job;
import com.lumens.sysdb.entity.Relation;
import com.lumens.sysdb.entity.Project;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobKey;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultScheduler implements JobScheduler {
    boolean isStarted;
    Scheduler sched;
    JobListener listener;
    List<Job> jobList = new ArrayList();
    Map<Long, Job> jobMap = new HashMap<>();
    Map<Long, List<Project>> projectMap = new HashMap<>();
    TransformEngine engine;

    // TODO: maintain a running job list
    public DefaultScheduler() {
        isStarted = false;
        start();
    }

    @Override
    public void setEngine(TransformEngine engine) {
        this.engine = engine;
    }

    @Override
    public JobScheduler addSchedule(DefaultJob job, JobTrigger trigger) {
        if (jobMap.containsKey(job.getId())) {
            throw new RuntimeException("Job " + job.getId() + " already exist.");
        }

        Job dbJob = new Job(job.getId(), job.getName(), job.getDescription(),
                trigger.getRepeatCount(), trigger.getRepeatInterval(),
                new Timestamp(trigger.getStartTime().getTime()), new Timestamp(trigger.getEndTime().getTime()));
        jobList.add(dbJob);
        long jobId = job.getId();
        jobMap.put(jobId, dbJob);
        ProjectDAO projectDAO = DAOFactory.getProjectDAO();
        List<Project> projectList = projectMap.get(jobId);
        if (projectList == null) {
            projectList = new ArrayList();
            projectMap.put(jobId, projectList);
        }
        for (long projectId : job.getProjectList()) {
            Project dbProject = projectDAO.getProject(projectId);
            if (dbProject == null) {
                throw new RuntimeException("Invalid project");
            }
            projectList.add(dbProject);
        }

        return this;
    }

    @Override
    public void startJob(long jobId) {
        Job job = jobMap.get(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before start.");
        }

        String group = String.valueOf(job.id);
        List<Project> projectList = projectMap.get(jobId);
        for (Project proj : projectList) {
            JobDetail jobDetail = newJob(JobThread.class)
                    .withIdentity(String.valueOf(proj.id), group)
                    .usingJobData("ProjectData", proj.data)
                    .usingJobData("ProjectName", proj.name)
                    .build();

            jobDetail.getJobDataMap().put("EngineObject", this.engine);
            SimpleScheduleBuilder simpleBuilder = simpleSchedule();
            int repeatCount = job.repeatCount;
            if (repeatCount > 0) {
                simpleBuilder.withRepeatCount(repeatCount);
            } else if (repeatCount < 0) {
                simpleBuilder.repeatForever();
            }
            int repeatInterval = job.interval;
            if (repeatCount != 0 && repeatInterval > 0) {
                simpleBuilder.withIntervalInSeconds(repeatInterval);
            }

            TriggerBuilder<Trigger> builder = newTrigger();
            builder.withIdentity(String.valueOf(job.id), group);
            builder.withSchedule(simpleBuilder);
            builder.startAt(job.startTime);
            builder.endAt(job.endTime);

            try {
                sched.scheduleJob(jobDetail, builder.build());
            } catch (SchedulerException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void stopJob(long jobId) {
        Job job = jobMap.get(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before stop.");
        }

        // Remove from map and scheduler
        String group = String.valueOf(jobId);
        List<Project> projectList = projectMap.get(jobId);
        for (Project proj : projectList) {
            try {
                sched.deleteJob(new JobKey(String.valueOf(proj.id), group));
            } catch (SchedulerException ex) {
                // TODO: log error 
            }
        }
    }

    @Override
    public void saveJob(long jobId) {
        Job job = jobMap.get(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before saving.");
        }

        JobDAO jobDAO = DAOFactory.getJobDAO();
        RelationDAO projectRelationDAO = DAOFactory.getRelationDAO();

        if (jobDAO.getJob(jobId) == null) {
            jobDAO.create(job);
        } else {
            jobDAO.update(job);
        }

        // Delete old relation
        projectRelationDAO.deleteAllRelation(jobId);
        for (Project project : projectMap.get(jobId)) {
            projectRelationDAO.create(jobId, project.id);
        }
    }

    @Override
    public void deleteJob(long jobId) {
        Job job = jobMap.remove(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before deleting.");
        }

        jobList.remove(job);

        // Remove from map and scheduler
        String group = String.valueOf(job.id);
        List<Project> projectList = projectMap.remove(jobId);
        for (Project proj : projectList) {
            try {
                sched.deleteJob(new JobKey(String.valueOf(proj.id), group));
            } catch (SchedulerException ex) {
                // TODO: log error 
            }
        }

        // Remove from db
        JobDAO jobDAO = DAOFactory.getJobDAO();
        RelationDAO projectRelationDAO = DAOFactory.getRelationDAO();

        if (jobDAO.getJob(jobId) != null) {
            jobDAO.delete(jobId);
            projectRelationDAO.deleteAllRelation(jobId);
        }
    }

    @Override
    public void resume() {
        try {
            loadFromDb();
        } catch (Exception ex) {
            // TODO: log load job exception
        }
        scheduleAll();
    }

    @Override
    public final void start() {
        try {
            if (!isStarted) {
                sched = new org.quartz.impl.StdSchedulerFactory().getScheduler();
                sched.start();
                listener = new DefaultJobListener();
                sched.getListenerManager().addJobListener(listener);
            }

        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void stop() {
        try {
            if (isStarted) {
                sched.shutdown(false);  // no wait of tasks finished
            }
        } catch (SchedulerException ex) {
            // TODO: Print log
        }
    }

    public List<Job> getJobList() {
        return jobList;
    }

    private void scheduleAll() {
        for (Job job : jobList) {
            startJob(job.id);
        }
    }

    private List<Job> loadJobFromDb() {
        JobDAO jobDAO = DAOFactory.getJobDAO();
        return jobDAO.getAllJob();
    }

    private List<Project> loadProjectFromDb(long jobId) {
        List<Project> projectList = new ArrayList();
        RelationDAO relationDAO = DAOFactory.getRelationDAO();
        ProjectDAO projectDAO = DAOFactory.getProjectDAO();
        List<Relation> relationList = relationDAO.getAllRelation(jobId);
        for (Relation relation : relationList) {
            projectList.add(projectDAO.getProject(relation.projectId));
        }

        return projectList;
    }

    public void loadFromDb() throws Exception {
        jobList.clear();
        projectMap.clear();

        List<Job> allJob = loadJobFromDb();
        for (Job dbJob : allJob) {
            jobList.add(dbJob);
            long jobId = dbJob.id;
            projectMap.put(jobId, loadProjectFromDb(jobId));
        }
    }

    public void saveToDb() {
        JobDAO jobDAO = DAOFactory.getJobDAO();
        RelationDAO projectRelationDAO = DAOFactory.getRelationDAO();

        // Save a job
        for (Job dbJob : jobList) {
            long jobId = dbJob.id;
            if (jobDAO.getJob(jobId) == null) {
                jobDAO.create(dbJob);
            } else {
                jobDAO.update(dbJob);
            }

            // Delete old relation
            projectRelationDAO.deleteAllRelation(jobId);
            for (Project project : projectMap.get(jobId)) {
                projectRelationDAO.create(jobId, project.id);
            }
        }
    }
}
