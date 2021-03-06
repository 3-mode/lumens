/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.entity;

import com.lumens.sysdb.Column;
import com.lumens.sysdb.Table;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
@Table(name = "LUNS_JOB_PROJECT_RELATION")
public class JobProjectRelation {

    @Column(name = "job_id")
    public long jobId;
    @Column(name = "project_id")
    public long projectId;

    public JobProjectRelation(long jobId, long projectId) {
        this.jobId = jobId;
        this.projectId = projectId;
    }

    public JobProjectRelation() {
    }
}
