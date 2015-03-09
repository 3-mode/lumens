/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface JobTriggerBuilder {
    JobTrigger build();
    JobTriggerBuilder withStartTime(long startTime);
    JobTriggerBuilder withEndTime(long startTime);
    JobTriggerBuilder withRepeat(int repeat);
    JobTriggerBuilder withInterval(int interval);
}
