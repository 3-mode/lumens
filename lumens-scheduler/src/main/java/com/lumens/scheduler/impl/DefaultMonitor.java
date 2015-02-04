/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.scheduler.JobMonitor;
import java.util.List;
import java.util.ArrayList;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultMonitor implements JobMonitor, JobListener {

    Scheduler sched;
    String name;
    List<String> pendingJobList = new ArrayList();
    List<String> runningJobList = new ArrayList();

    public DefaultMonitor(Scheduler scheduler) {
        this.sched = scheduler;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getPendingJobIdList() {
        return pendingJobList;
    }

    @Override
    public List<String> getRunningJobIdList() {
        return runningJobList;
    }

    @Override
    public List<String> getRunningProjectIdList(long jobId) {
        return null;
    }

    @Override
    public String getProjectStatus(long jobId) {
        return null;
    }

    // Job listener implementation
    @Override
    public void jobToBeExecuted(JobExecutionContext jec) {
        pendingJobList.add(jec.getJobDetail().getKey().getGroup());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jec) {
        String jobId = jec.getJobDetail().getKey().getGroup();
        pendingJobList.remove(jobId);
        runningJobList.remove(jobId);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jec, JobExecutionException jee) {
        String jobId = jec.getJobDetail().getKey().getGroup();
        pendingJobList.remove(jobId);
        runningJobList.add(jobId);
    }
}
