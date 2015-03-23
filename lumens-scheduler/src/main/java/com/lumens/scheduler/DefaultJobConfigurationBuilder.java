/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.scheduler.impl.DefaultJobConfiguration;
import com.lumens.sysdb.entity.Job;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJobConfigurationBuilder {

    public static JobConfiguration build(Job job) {
        return new DefaultJobConfiguration(job.id, job.name, job.description, job.startTime.getTime(), job.endTime.getTime(), job.repeat, job.interval);
    }
}
