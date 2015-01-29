/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class JobThread implements Job {
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDetail job = jec.getJobDetail();
        String jobId = job.getKey().getGroup();
        String projectId = job.getKey().getName();

        System.out.println("Executing Job: " + jobId + " Project:" + projectId);

        String projectData = jec.getMergedJobDataMap().getString("Project");
        System.out.println("Project date = " + projectData);
        // TODO: sent project id to Application global context to execute, or execute here
    }}
