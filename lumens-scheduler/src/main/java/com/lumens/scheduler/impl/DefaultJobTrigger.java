/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.scheduler.JobTrigger;
import java.util.Date;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJobTrigger implements JobTrigger {
    private final long startTime;
    private final long endTime;
    private final int repeat;
    private final int interval;

    public DefaultJobTrigger(long startTime, long endTime, int repeat, int interval) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeat = repeat;
        this.interval = interval;
    }

    @Override
    public int getRepeat() {
        return repeat;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    // In millisecond
    @Override
    public int getInterval() {
        return interval;
    }
}
