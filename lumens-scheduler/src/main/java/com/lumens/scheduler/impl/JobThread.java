/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformEngine;
import com.lumens.engine.TransformProject;
import com.lumens.log.DataElementLoggingHandler;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import java.io.ByteArrayInputStream;
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
public class JobThread implements Job {

    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDetail job = jec.getJobDetail();
        String jobId = job.getKey().getGroup();
        String projectId = job.getKey().getName();
        System.out.println("Executing Job: " + jobId + " Project:" + projectId);

        TransformEngine engine = (TransformEngine) job.getJobDataMap().get("EngineObject");
        TransformProject projectInstance = new TransformProject();
        try {
            String projectData = jec.getMergedJobDataMap().getString("ProjectData");
            String projectName = jec.getMergedJobDataMap().getString("ProjectName");
            new ProjectSerializer(projectInstance).readFromJson(new ByteArrayInputStream(projectData.getBytes()));

            // Execute all start rules to drive the ws connector
            List<ResultHandler> handlers = new ArrayList<>();
            handlers.add(new DataElementLoggingHandler(Long.parseLong(projectId), projectName));
            engine.execute(new SequenceTransformExecuteJob(projectInstance, handlers));
        } catch (Exception ex) {
            System.out.println("Fail to execute Job: " + jobId + " Project:" + projectId);
        }
    }
}
