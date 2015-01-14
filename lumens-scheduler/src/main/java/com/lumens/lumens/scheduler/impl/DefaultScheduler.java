/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.lumens.scheduler.impl;

import com.lumens.lumens.scheduler.JobScheduler;
import java.util.List;
import java.util.ArrayList;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.SchedulerFactory;
import org.quartz.Scheduler;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultScheduler implements JobScheduler {
    Scheduler sched; 
    List<DefaultJob> jobList = new ArrayList();

    public DefaultScheduler() {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        try {
            sched = schedFact.getScheduler(); 
            sched.start();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void resume(){
        
    }
    
    public List<Long> getJobList() {
        return null;
    }

    public void loadFromDb() {

    }

    public void saveToDb() {

    }

}
