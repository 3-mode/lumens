/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.scheduler.JobTrigger;
import com.lumens.scheduler.JobTriggerBuilder;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJobTriggerBuilder implements JobTriggerBuilder {

    long startTime = -1;
    long endTime = -1;
    int repeat = -1;  // Never repeat by default
    int interval = -1;

    @Override
    public JobTrigger build() {
        if (startTime == -1){
            startTime = System.currentTimeMillis();
        }
        if (endTime == -1){
            endTime = startTime + 1000;
        }
            
        return new DefaultJobTrigger(startTime, endTime, repeat, interval);
    }

    @Override
    public JobTriggerBuilder withStartTime(long startTime){
        this.startTime = startTime;
        return this;
    }

    @Override
    public JobTriggerBuilder withEndTime(long endTime){
        if ( endTime - startTime < 0){
            throw new RuntimeException("End time must later than start time.");
        }
        this.endTime = endTime;
        return this;
    }

    @Override
    public JobTriggerBuilder withRepeat(int repeat){
        this.repeat = repeat;
         return this;
    }

    @Override
    public JobTriggerBuilder withInterval(int interval){
        this.interval = interval;
         return this;
    }
}
