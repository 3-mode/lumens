/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import java.util.Calendar;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class SimpleJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("Execute job " + this + " -- " + Calendar.getInstance().getTime());
    }
}