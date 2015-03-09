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

    List<Long> projectIdList = new ArrayList();

    public DefaultJob(long jobId, String name, String description) {
        this.jobId = jobId;
        this.name = name;
        this.description = description;
    }

    public DefaultJob() {
        this.jobId = 0;
        this.name = "default";
        this.description = "";
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
    public Job addProject(long projectId) {
        projectIdList.add(projectId);

        return this;
    }

    @Override
    public List<Long> getProjectList() {
        return projectIdList;
    }
}
