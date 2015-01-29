/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.dao;

import com.lumens.sysdb.entity.Job;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class JobDAO extends BaseDAO {
    public int getTotal() {
        return super.getTotal("JobDAO/total");
    }

    public long create(final Job job) {
        simplePrepareStatTransactionExecute(sqlManager.getSQL("JobDAO/CreateJob"), job.id, job.name, job.description, job.repeatCount, job.interval, job.startTime, job.endTime);
        return job.id;
    }

    public long update(final Job job) {
        simplePrepareStatTransactionExecute(sqlManager.getSQL("JobDAO/UpdateJob"), job.id, job.name, job.description, job.repeatCount, job.interval, job.startTime, job.endTime);
        return job.id;
    }

    public long delete(final long jobId) {
        simplePrepareStatTransactionExecute(sqlManager.getSQL("JobDAO/DeleteJob"), jobId);
        return jobId;
    }

    public Job getJob(long jobId) {
        List<Job> pList = simpleQuery(sqlManager.getSQL("JobDAO/FindJob", jobId), Job.class);
        return pList.size() > 0 ? pList.get(0) : null;
    }

    public List<Job> getAllJob() {
        return simpleQuery(sqlManager.getSQL("JobDAO/AllJob"), Job.class);
    }
}
