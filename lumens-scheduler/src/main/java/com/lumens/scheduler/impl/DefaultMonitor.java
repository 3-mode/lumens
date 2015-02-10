/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.scheduler.JobMonitor;
import com.lumens.scheduler.JobScheduler;
import com.lumens.sysdb.entity.Project;
import com.lumens.sysdb.utils.DBHelper;
import java.util.List;
import java.util.ArrayList;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultMonitor implements JobMonitor, JobListener {
    JobScheduler sched;
    String name;
    List<String> pendingJobList = new ArrayList();
    List<String> runningJobList = new ArrayList();

    public DefaultMonitor(JobScheduler scheduler) {
        name = "Default Monitor";
        this.sched = scheduler;
        sched.registerJobListener(this);        
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
        return DBHelper.loadProjectIdFromDb(jobId);    
    }

    @Override
    public List<Project> getProjectList(long jobId) {
        return DBHelper.loadProjectFromDb(jobId);
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
