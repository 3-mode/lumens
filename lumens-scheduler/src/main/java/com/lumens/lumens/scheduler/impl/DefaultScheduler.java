/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.lumens.scheduler.impl;

import com.lumens.lumens.scheduler.JobScheduler;
import java.util.List;
import org.quartz.SchedulerFactory;
import org.quartz.Scheduler;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultScheduler implements JobScheduler{
    public List<Long> getJobList(){
        return null;
    }
}
