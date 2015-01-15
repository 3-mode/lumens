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
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultScheduler implements JobScheduler {

    Scheduler sched;
    List<DefaultJob> jobList = new ArrayList();
    Map<Long, DefaultJob> jobMap = new HashMap<>();
    Map<Long, DefaultTrigger> triggerMap = new HashMap<>();

    public DefaultScheduler() {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        try {
            sched = schedFact.getScheduler();
            sched.start();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public JobScheduler addSchedule(DefaultJob job, DefaultTrigger trigger) {
        if (jobMap.containsKey(job.getId())) {
            throw new RuntimeException("Job " + job.getId() + " already exist.");
        }

        jobList.add(job);
        jobMap.put(job.getId(), job);
        triggerMap.put(trigger.getId(), trigger);

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
                TriggerBuilder<Trigger> builder = newTrigger();
                builder.withIdentity(defaultTrigger.getId().toString(), group).startNow();                
                builder.withSchedule(simpleSchedule().withIntervalInSeconds(defaultTrigger.getRepeatInterval()));
                if (defaultTrigger.getRepeatCount() <= 0){
                    
                }
                try {
                    sched.scheduleJob(jobDetail, builder.build());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public void shutdown() {
        try {
            sched.shutdown();
        } catch (SchedulerException ex) {
            // TODO: log exception
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
