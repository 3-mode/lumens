/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.scheduler.JobMonitor;
import com.lumens.scheduler.JobScheduler;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultMonitor implements JobMonitor, JobListener {

    Scheduler sched;
    String name;

    public DefaultMonitor(Scheduler scheduler) {
        this.sched = scheduler;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jec) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jec) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext jec, JobExecutionException jee) {

    }

    @Override
    public List<String> getRunningJobIdList() {
        List<String> jobIdList = null;

        try {
            jobIdList = sched.getJobGroupNames();
        } catch (SchedulerException ex) {

        }
        return jobIdList;
    }

    @Override
    public List<String> getRunningProjectIdList(long jobId) {
        List<String> projectIdList = null;

        return projectIdList;
    }

    @Override
    public String getProjectStatus(long jobId) {
        return null;
    }
}
