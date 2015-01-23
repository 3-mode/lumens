/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.scheduler.JobScheduler;
import com.lumens.scheduler.JobTrigger;
import com.lumens.engine.TransformProject;
import com.lumens.engine.serializer.ProjectSerializer;
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
import org.quartz.JobDetail;
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
    List<Job> jobList = new ArrayList();
    Map<Long, Job> jobMap = new HashMap<>();
    Map<Long, List<Project>> projectMap = new HashMap<>();

    // TODO: maintain a running job list
    public DefaultScheduler() {
        isStarted = false;
        start();
    }

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
        if (projectList == null){
            projectList = new ArrayList();
            projectMap.put(jobId, projectList);
        }
        for (long projectId: job.getProjectList()){
            Project dbProject = projectDAO.getProject(projectId);
            if (dbProject == null)
                throw new RuntimeException("Invalid project");
            projectList.add(dbProject);
        }

        return this;
    }

    @Override
    public void resume() {
        try {
            loadFromDb();
        } catch (Exception ex) {
            // TODO: log load job exception
        }
        schedule();
    }

    @Override
    public void schedule() {
        for (Job job : jobList) {
            long jobId = job.id;
            String group = String.valueOf(job.id);
            List<Project> projectList = projectMap.get(jobId);
            for (Project proj : projectList) {
                JobDetail jobDetail = newJob(DefaultJob.class)
                        .withIdentity(String.valueOf(proj.id), group)
                        .build();
         
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
                builder.startNow();
                builder.withSchedule(simpleBuilder);
                builder.startAt(job.startTime);

                try {
                    sched.scheduleJob(jobDetail, builder.build());
                } catch (SchedulerException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public final void start() {
        try {
            if (!isStarted) {
                sched = new org.quartz.impl.StdSchedulerFactory().getScheduler();
                sched.start();
            }

        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void stop() {
        try {
            if (isStarted) {
                sched.shutdown();
            }
        } catch (SchedulerException ex) {
            // TODO: Print log
        }
    }

    public List<Job> getJobList() {
        return jobList;
    }

    private List<Job> loadJobFromDb() {
        List<Job> jobList = new ArrayList();
        JobDAO jobDAO = DAOFactory.getJobDAO();
        jobList.addAll(jobDAO.getAllJob());

        return jobList;
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
