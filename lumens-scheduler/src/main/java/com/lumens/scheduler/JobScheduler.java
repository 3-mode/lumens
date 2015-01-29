/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.engine.TransformEngine;
import com.lumens.scheduler.impl.DefaultJob;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface JobScheduler {
    public void setEngine(TransformEngine engine);

    public JobScheduler addSchedule(DefaultJob job, JobTrigger trigger);

    public void startJob(long jobId);

    public void stopJob(long jobId);

    public void saveJob(long jobId);

    public void deleteJob(long jobId);

    public void resume();

    public void start();

    public void stop();
}
