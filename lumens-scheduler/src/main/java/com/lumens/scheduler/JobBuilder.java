/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.scheduler;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface JobBuilder {
    JobConfiguration build();
    JobBuilder withJobId(long jobId);
    JobBuilder withJobName(String jobName);
    JobBuilder withJobDescription(String jobDescription);
    JobBuilder withStartTime(long jobId);
    JobBuilder withEndTime(long jobId);
    JobBuilder withRepeat(int repeat);
    JobBuilder withInterval(int interval);
}
