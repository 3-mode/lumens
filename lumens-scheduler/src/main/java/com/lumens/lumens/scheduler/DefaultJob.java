/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.lumens.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJob implements Job{
    public void execute(JobExecutionContext jec) throws JobExecutionException{     
        System.out.println("Hello! Default job is running");
    }
}
