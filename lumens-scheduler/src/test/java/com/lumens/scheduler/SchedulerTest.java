/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.scheduler.impl.DefaultScheduler;
import com.lumens.scheduler.impl.DefaultTrigger;
import com.lumens.scheduler.impl.DefaultJob;
import com.lumens.scheduler.SchedulerFactory;
import java.util.Date;
import com.lumens.engine.TransformProject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SchedulerTest     
{
    @Before
    public void Before() {
    }

    @Test
    public void SchedulerTest() {
        DefaultScheduler scheduler = (DefaultScheduler)SchedulerFactory.get().createScheduler();
        scheduler.start();        
        TransformProject project = new TransformProject();
        project.setName("Test project");
        DefaultTrigger trigger = new DefaultTrigger(new Date(System.currentTimeMillis() + 10000), new Date(), 1,1 );
        
        DefaultJob job = new DefaultJob(1001, "job1001", "This is a sample job");
        job.addProject(project);
        scheduler.addSchedule(job, trigger);
        scheduler.start();
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {       
            throw new RuntimeException(ex);
        }
    }
    
    @Test
    public void QuartzTest(){
        org.quartz.SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        try {
            Scheduler sched = schedFact.getScheduler();
            sched.start();

            JobDetail job = newJob(DefaultJob.class)
                    .withIdentity("defaultJob", "group1")
                    .build();
                        
            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = newTrigger()
                    .withIdentity("defaultTrigger", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(2)
                            .repeatForever())
                    .build();
            
            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(job, trigger);
            
            Thread.sleep(10000);
            sched.shutdown();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
