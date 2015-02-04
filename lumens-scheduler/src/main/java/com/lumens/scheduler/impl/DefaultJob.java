/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJob {
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

    public Long getId() {
        return jobId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DefaultJob addProject(long projectId) {
        projectIdList.add(projectId);

        return this;
    }

    public List<Long> getProjectList() {
        return projectIdList;
    }
}
