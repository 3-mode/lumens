/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformEngine;
import com.lumens.engine.TransformProject;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.scheduler.JobConstants;
import java.util.ArrayList;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class JobExecutor implements Job {
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDetail job = jec.getJobDetail();
        String jobId = job.getKey().getGroup();
        String projectId = job.getKey().getName();
        System.out.println("Executing Job: " + jobId + " Project:" + projectId);

        TransformEngine engine = (TransformEngine) job.getJobDataMap().get(JobConstants.ENGINE_OBJECT);
        TransformProject project = (TransformProject)job.getJobDataMap().get(JobConstants.PROJECT_OBJECT);
        try {
            // Execute all start rules to drive the ws connector
            List<ResultHandler> handlers = new ArrayList<>();
            // TODO
            // handlers.add(new DataElementLoggingHandler(Long.parseLong(projectId), projectName));
            engine.execute(new SequenceTransformExecuteJob(project, handlers));
        } catch (Exception ex) {
            System.out.println("Fail to execute Job: " + jobId + " Project:" + projectId);
            ex.printStackTrace();
        }
    }
}
