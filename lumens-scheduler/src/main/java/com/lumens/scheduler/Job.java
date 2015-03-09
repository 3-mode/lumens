/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface Job {

    public Long getId();

    public String getName();

    public String getDescription();

    public Job addProject(long projectId);

    public List<Long> getProjectList();
}
