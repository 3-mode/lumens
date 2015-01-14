/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.lumens.scheduler.impl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.Date;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJob implements Job {

    private long jobId;
    private String name;
    private String description;

    public DefaultJob(long jobId, String name, String description) {
        this.jobId = jobId;
        this.name = name;
        this.description = description;
    }

    public long getJobId() {
        return jobId;
    }

    public long getJobName() {
        return name;
    }

    public long getJobDescription() {
        return description;
    }

    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("[" + (new Date()) + "]" + jec.getTrigger().getKey().getName() + " job is running");
    }

    public void loadJobFromDb() {

    }

    public void saveJobToDb() {

    }
}
