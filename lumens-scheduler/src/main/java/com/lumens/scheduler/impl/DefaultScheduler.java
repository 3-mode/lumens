/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformEngine;
import com.lumens.scheduler.JobScheduler;
import com.lumens.scheduler.Job.Repeat;
import com.lumens.scheduler.JobMonitor;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.dao.JobProjectRelationDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.scheduler.Job;
import com.lumens.sysdb.entity.Project;
import com.lumens.sysdb.utils.DBHelper;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobKey;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.ScheduleBuilder;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
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
    JobMonitor jobMonitor;
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
    public JobMonitor getJobMonitor() {
        return jobMonitor;
    }

    @Override
    public void registerJobListener(JobListener listener) {
        try {
            sched.getListenerManager().addJobListener(listener);
        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void unRegisterJobListener(JobListener listener) {
        try {
            sched.getListenerManager().removeJobListener(listener.getName());
        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setEngine(TransformEngine engine) {
        this.engine = engine;
    }

    @Override
    public JobScheduler addSchedule(Job job) {
        if (jobMap.containsKey(job.getId())) {
            throw new RuntimeException("Job " + job.getId() + " already exist.");
        }

        jobList.add(job);
        long jobId = job.getId();
        jobMap.put(jobId, job);
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

        String group = Long.toString(job.getId());
        List<Project> projectList = projectMap.get(jobId);
        for (Project proj : projectList) {
            JobDetail jobDetail = newJob(JobThread.class)
                    .withIdentity(Long.toString(proj.id), group)
                    .usingJobData("ProjectData", proj.data)
                    .usingJobData("ProjectName", proj.name)
                    .build();
            jobDetail.getJobDataMap().put("EngineObject", this.engine);

            TriggerBuilder<Trigger> builder = newTrigger();
            builder.withIdentity(Long.toString(job.getId()), group);
            builder.withSchedule(getQuartzBuilder(job.getRepeat(), job.getInterval()));   
            builder.startAt(new Date(job.getStartTime()));
            // TODO: add end time

            try {
                sched.scheduleJob(jobDetail, builder.build());
            } catch (SchedulerException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private ScheduleBuilder getQuartzBuilder(int repeat, int interval) {
        if (interval < 0) {
            throw new RuntimeException("Illegal value: scheduler interval should not be less than 0.");
        }

        switch (Repeat.valueOf(repeat)) {
            case Secondly:
                return simpleSchedule().withIntervalInSeconds(interval).repeatForever();
            case Minutely:
                return simpleSchedule().withIntervalInMinutes(interval).repeatForever();
            case Hourly:
                return simpleSchedule().withIntervalInHours(interval).repeatForever();
            case Daily:
                return simpleSchedule().withIntervalInHours(interval).repeatForever();
            case Weekly:
                return simpleSchedule().withIntervalInHours(interval).repeatForever();
            case Monthly:
                return calendarIntervalSchedule().withIntervalInMonths(interval);
            case Yearly:
                return calendarIntervalSchedule().withIntervalInYears(interval);
            case Never:
                break;

            default:
                throw new RuntimeException("Illegal value: scheduler repeat not available" + Repeat.valueOf(repeat).toString());
        }

        return null;
    }

    @Override
    public void stopJob(long jobId) {
        Job job = jobMap.get(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before stop.");
        }

        // Remove from map and scheduler
        String group = Long.toString(jobId);
        List<Project> projectList = projectMap.get(jobId);
        for (Project proj : projectList) {
            try {
                sched.deleteJob(new JobKey(Long.toString(proj.id), group));
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
        
        // TODO: save to db via web service 
    }

    @Override
    public void deleteJob(long jobId) {
        Job job = jobMap.remove(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before deleting.");
        }

        jobList.remove(job);

        // Remove from map and scheduler
        String group = Long.toString(job.getId());
        List<Project> projectList = projectMap.remove(jobId);
        for (Project proj : projectList) {
            try {
                sched.deleteJob(new JobKey(Long.toString(proj.id), group));
            } catch (SchedulerException ex) {
                // TODO: log error 
            }
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
                jobMonitor = new DefaultMonitor(this);
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
            startJob(job.getId());
        }
    }

    public void loadFromDb() throws Exception {
        jobList.clear();
        projectMap.clear();
    }

    public void saveToDb() {
    }
}
