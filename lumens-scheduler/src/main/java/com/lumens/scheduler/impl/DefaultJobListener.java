/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJobListener implements JobListener {
    String name;
    public DefaultJobListener(){
        name = "Default Listener";
    }
    
    @Override
    public String getName(){
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jec){
        
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jec){
        
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jec, JobExecutionException jee){
        
    }
}
