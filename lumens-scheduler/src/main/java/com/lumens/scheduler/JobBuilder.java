/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.scheduler;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface JobBuilder {
    Job build();
    JobBuilder withJobId(long jobId);
    JobBuilder withJobName(String jobName);
    JobBuilder withJobDescription(String jobDescription);
}
