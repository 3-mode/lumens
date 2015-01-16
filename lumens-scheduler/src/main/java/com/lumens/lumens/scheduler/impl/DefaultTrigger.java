/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

package com.lumens.lumens.scheduler.impl;

import com.lumens.lumens.scheduler.Trigger;
import java.util.Date;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.DateBuilder.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultTrigger implements Trigger{
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
