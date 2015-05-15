/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformEngine;
import com.lumens.engine.TransformProject;
import com.lumens.logsys.LogSysFactory;
import com.lumens.scheduler.JobScheduler;
import com.lumens.scheduler.JobConfiguration.Repeat;
import com.lumens.scheduler.JobMonitor;
import com.lumens.scheduler.JobConfiguration;
import com.lumens.scheduler.JobConstants;
import com.lumens.sysdb.entity.Project;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
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

    private final Logger log = LogSysFactory.getLogger(DefaultScheduler.class);
    private final boolean isStarted;
    private Scheduler sched;
    private JobMonitor jobMonitor;
    private TransformEngine engine;
    private final List<JobConfiguration> jobList = new ArrayList();
    private final Map<Long, JobConfiguration> jobMap = new HashMap<>();
    private final Map<Long, List<Project>> projectMap = new HashMap<>();

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
    public JobScheduler addSchedule(JobConfiguration job) {
        if (jobMap.containsKey(job.getId())) {
            throw new RuntimeException("Job " + job.getId() + " already exist.");
        }

        jobList.add(job);
        long jobId = job.getId();
        jobMap.put(jobId, job);

        return this;
    }

    @Override
    public void startJob(long jobId) {
        JobConfiguration job = jobMap.get(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before start.");
        }
        try {
            String group = Long.toString(job.getId());
            JobDetail jobDetail = newJob(JobExecutor.class)
            .withIdentity(job.getName(), group)
            .build();
            jobDetail.getJobDataMap().put(JobConstants.JOB_CONFIG, job);
            jobDetail.getJobDataMap().put(JobConstants.TRNASFORM_ENGINE, this.engine);

            TriggerBuilder<Trigger> builder = newTrigger();
            builder.withIdentity(Long.toString(job.getId()), group);
            builder.withSchedule(getQuartzBuilder(job.getRepeat(), job.getInterval()));
            builder.startAt(new Date(job.getStartTime()));

            sched.scheduleJob(jobDetail, builder.build());
        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
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
        JobConfiguration job = jobMap.get(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before stop.");
        }

        // Remove from map and scheduler
        String group = Long.toString(jobId);
        try {
            sched.deleteJob(new JobKey(job.getName(), group));
        } catch (SchedulerException ex) {
            log.error(ex);
        }
    }

    @Override
    public void saveJob(long jobId) {
        JobConfiguration job = jobMap.get(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before saving.");
        }
        // TODO: save to db via web service 
    }

    @Override
    public void deleteJob(long jobId) {
        JobConfiguration job = jobMap.remove(jobId);
        if (job == null) {
            throw new RuntimeException("A job must be added to scheduler before deleting.");
        }

        jobList.remove(job);

        // Remove from map and scheduler
        String group = Long.toString(job.getId());
        for (TransformProject proj : job.getProjectList()) {
            try {
                sched.deleteJob(new JobKey(proj.getName(), group));
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
                jobMonitor = new DefaultJobMonitor(this);
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

    public List<JobConfiguration> getJobList() {
        return jobList;
    }

    private void scheduleAll() {
        for (JobConfiguration job : jobList)
            startJob(job.getId());
    }

    public void loadFromDb() throws Exception {
        jobList.clear();
        projectMap.clear();
    }

    public void saveToDb() {
    }
}
