/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public interface JobMonitor {
    public List<String> getRunningJobIdList();

    public List<String> getPendingJobIdList();

    public List<String> getRunningProjectIdList(long jobId);

    public String getProjectStatus(long JobId);
}
