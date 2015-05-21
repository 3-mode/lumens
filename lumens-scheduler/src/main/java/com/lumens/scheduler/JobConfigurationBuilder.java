/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.logsys.JobLogFactory;
import com.lumens.scheduler.impl.ScheduleJobConfiguration;
import com.lumens.sysdb.entity.Job;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class JobConfigurationBuilder {

    public static JobConfiguration build(Job job) {
        return JobConfigurationBuilder.build(job, null);
    }

    public static JobConfiguration build(Job job, JobLogFactory jobLogFactory) {
        if (jobLogFactory != null)
            jobLogFactory.start();
        return new ScheduleJobConfiguration(jobLogFactory, job.id, job.name, job.description, job.startTime.getTime(), job.endTime.getTime(), job.repeat, job.interval);
    }
}
