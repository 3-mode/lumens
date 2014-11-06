package com.lumens.task;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.junit.Test;

public class TaskManagementTest {

    @Test
    public void testScheduler() throws Exception {

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail job = JobBuilder.newJob(SimpleJob.class)
        .withIdentity("job1", "group1")
        .build();

        Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("myTrigger", "group1")
        .startNow()
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
        .withIntervalInSeconds(40)
        .repeatForever())
        .build();

        sched.scheduleJob(job, trigger);
        sched.start();
        System.in.read();
    }
}
