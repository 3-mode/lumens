/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.scheduler.Job;
import com.lumens.scheduler.JobBuilder;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJobBuilder implements JobBuilder {

    long jobId = -1;
    String jobName;
    String jobDescription;

    @Override
    public Job build() {
        if (jobId == -1 )
            throw new RuntimeException("Job ID could not be null."); // TODO: generate id from last db id
        if (jobName == null)
            jobName = "DefaultJobName";
        if (jobDescription == null)
            jobDescription = "Default job description";
        return new DefaultJob(jobId, jobName, jobDescription);
    }

    @Override
    public JobBuilder withJobId(long jobId) {
        this.jobId = jobId;
        return this;
    }

    @Override
    public JobBuilder withJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    @Override
    public JobBuilder withJobDescripttion(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }
}
