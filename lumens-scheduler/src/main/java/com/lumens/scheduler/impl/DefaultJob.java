/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformProject;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.entity.Project;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDetail;
import java.util.List;

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

    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDetail job = jec.getJobDetail();
        String jobId = job.getKey().getGroup();
        String projectId = job.getKey().getName();
        
        System.out.println("Executing Job: " + jobId + " Project:" + projectId);
        
        String projectData = jec.getMergedJobDataMap().getString("Project");
        System.out.println("Project date = " + projectData);        
        // TODO: sent project id to Application global context to execute, or execute here
    }
}
