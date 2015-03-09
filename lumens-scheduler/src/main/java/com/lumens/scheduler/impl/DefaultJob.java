/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import java.util.ArrayList;
import java.util.List;
import com.lumens.scheduler.Job;
/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJob implements Job {
    private final long jobId;
    private final String name;
    private final String description;
    private final long startTime;
    private final long endTime;
    private final int repeat;
    private final int interval;

    List<Long> projectIdList = new ArrayList();

    public DefaultJob(long jobId, String name, String description, long startTime, long endTime, int repeat, int interval) {
        this.jobId = jobId;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeat = repeat;
        this.interval = interval;
    }

    public DefaultJob() {
        this.jobId = 0;
        this.name = "default";
        this.description = "";
        this.startTime = System.currentTimeMillis();
        this.endTime = System.currentTimeMillis() + 1000;
        this.repeat = -1;
        this.interval = -1;
    }

    @Override
    public Long getId() {
        return jobId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRepeat() {
        return repeat;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    // In millisecond
    @Override
    public int getInterval() {
        return interval;
    }

    @Override
    public Job addProject(long projectId) {
        projectIdList.add(projectId);

        return this;
    }

    @Override
    public List<Long> getProjectList() {
        return projectIdList;
    }
}
