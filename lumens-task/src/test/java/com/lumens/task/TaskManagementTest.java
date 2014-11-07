package com.lumens.task;

import java.util.Properties;
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
        Properties properties = new Properties();
        properties.put("org.quartz.scheduler.instanceName", "TestScheduler");
        properties.put("org.quartz.scheduler.instanceId", "AUTO");
        properties.put("org.quartz.scheduler.skipUpdateCheck", "true");
        properties.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", "12");
        properties.put("org.quartz.threadPool.threadPriority", "5");
        properties.put("org.quartz.jobStore.misfireThreshold", "10000");
        properties.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.put("org.quartz.jobStore.useProperties", "true");
        properties.put("org.quartz.jobStore.dataSource", "lumensDS");
        properties.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.put("org.quartz.jobStore.isClustered", "false");
        properties.put("org.quartz.dataSource.lumensDS.driver", "org.apache.derby.jdbc.ClientDriver");
        properties.put("org.quartz.dataSource.lumensDS.URL", "jdbc:derby://localhost:1527/lumens");
        properties.put("org.quartz.dataSource.lumensDS.user", "lumens");
        properties.put("org.quartz.dataSource.lumensDS.password", "lumens");
        properties.put("org.quartz.dataSource.lumensDS.maxConnections", "5");

        SchedulerFactory sf = new StdSchedulerFactory(properties);
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
