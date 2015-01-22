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
public class DefaultTrigger implements JobTrigger{
    private final Date startTime;
    private final Date endTime;
    private final int repeatCount;
    private final int interval;
    
    public DefaultTrigger(Date startTime, Date endTime, int repeatCount, int interval){
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatCount = repeatCount;
        this.interval = interval;
    }    

    
    public int getRepeatCount(){
        return repeatCount;
    }
    
    public Date getStartTime(){
        return startTime;
    }
    
    public Date getEndTime(){
        return endTime;
    }
    
    // In millisecond
    public int getRepeatInterval(){
        return interval;
    }
}
