/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.lumens.scheduler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.SchedulerFactory;
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
    public void ConnectTest() {
    }
    
    @Test
    public void QuartzTest(){
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

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
