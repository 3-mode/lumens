/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.scheduler.impl.ScheduleJobConfiguration;
import com.lumens.sysdb.entity.Job;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class JobConfigurationBuilder {

    public static JobConfiguration build(Job job) {
        return new ScheduleJobConfiguration(job.id, job.name, job.description, job.startTime.getTime(), job.endTime.getTime(), job.repeat, job.interval);
    }
}
