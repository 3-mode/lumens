/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.lumens.scheduler.impl;

import com.lumens.lumens.scheduler.JobScheduler;
import com.lumens.engine.TransformProject;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.ScheduleBuilder;
import org.quartz.SchedulerFactory;
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
    List<DefaultJob> jobList = new ArrayList();
    Map<Long, DefaultJob> jobMap = new HashMap<>();
    Map<Long, DefaultTrigger> triggerMap = new HashMap<>();

    public DefaultScheduler() {
        isStarted = false;
        startup();
    }

    public JobScheduler addSchedule(DefaultJob job, DefaultTrigger trigger) {
        if (jobMap.containsKey(job.getId())) {
            throw new RuntimeException("Job " + job.getId() + " already exist.");
        }

        jobList.add(job);
        jobMap.put(job.getId(), job);
        triggerMap.put(job.getId(), trigger);

        return this;
    }

    public void resume() {
        loadFromDb();
        start();
    }

    public void start() {
        Iterator itor = jobList.iterator();
        while (itor.hasNext()) {
            DefaultJob job = (DefaultJob) itor.next();
            String group = job.getId().toString();

            List<TransformProject> projectList = job.getProjectList();
            Iterator itorP = projectList.iterator();
            while (itorP.hasNext()) {
                TransformProject proj = (TransformProject) itorP.next();
                JobDetail jobDetail = newJob(DefaultJob.class)
                        .withIdentity(proj.getName(), group)
                        .build();

                DefaultTrigger defaultTrigger = triggerMap.get(job.getId());
                SimpleScheduleBuilder simpleBuilder = simpleSchedule();

                int repeatCount = defaultTrigger.getRepeatCount();
                if (repeatCount > 0) {
                    simpleBuilder.withRepeatCount(repeatCount);
                } else if (repeatCount < 0) {
                    simpleBuilder.repeatForever();
                }
                int repeatInterval = defaultTrigger.getRepeatInterval();
                if (repeatCount != 0 && repeatInterval > 0) {
                    simpleBuilder.withIntervalInSeconds(repeatInterval);
                }

                TriggerBuilder<Trigger> builder = newTrigger();
                builder.withIdentity(job.getId().toString(), group);
                builder.startNow();
                builder.withSchedule(simpleBuilder);
                builder.startAt(defaultTrigger.getStartTime());

                try {
                    sched.scheduleJob(jobDetail, builder.build());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public void startup() {        
        try {
            if (!isStarted){
                sched = new org.quartz.impl.StdSchedulerFactory().getScheduler();
                sched.start();
            }            

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public void shutdown() {
        try {
            if (isStarted){
                sched.shutdown();
            }
        }catch(Exception ex){            
        }
    }

    public List<DefaultJob> getJobList() {
        return jobList;
    }

    public void loadFromDb() {

    }

    public void saveToDb() {

    }

}
