/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.lumens.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.Date;


/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJob implements Job{
    public void execute(JobExecutionContext jec) throws JobExecutionException{     
        System.out.println("[" + (new Date()) + "]" + jec.getTrigger().getKey().getName() + " job is running");
    }
}
