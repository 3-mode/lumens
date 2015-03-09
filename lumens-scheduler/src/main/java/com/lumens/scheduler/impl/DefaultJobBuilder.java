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
    long startTime = -1;
    long endTime = -1;
    int repeat = -1;
    int interval = -1;

    @Override
    public Job build() {
        if (jobId == -1) {
            throw new RuntimeException("Job ID could not be null."); // TODO: generate id from last db id
        }
        if (jobName == null) {
            jobName = "DefaultJobName";
        }
        if (jobDescription == null) {
            jobDescription = "Default job description";
        }
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
        if (endTime != -1 && startTime - endTime > 0){
            throw new RuntimeException("End time must later than start time.");
        }
        if (interval == -1 && repeat != -1) {
            interval = 1; // At leat run 1 time within 1 repeat
        }
        return new DefaultJob(jobId, jobName, jobDescription, startTime, endTime, repeat, interval);
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
    public JobBuilder withJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    @Override
    public JobBuilder withStartTime(long starTime){
        this.startTime = startTime;
        return this;
    }

    @Override
    public JobBuilder withEndTime(long endTime){
        this.endTime = endTime;
        return this;
    }

    @Override
    public JobBuilder withRepeat(int repeat){
        this.repeat = repeat;
        return this;
    }

    @Override
    public JobBuilder withInterval(int interval){
        this.interval = interval;
        return this;
    }
}
