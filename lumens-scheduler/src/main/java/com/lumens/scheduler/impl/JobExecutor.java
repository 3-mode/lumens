/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformEngine;
import com.lumens.engine.TransformProject;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.logsys.LogSysFactory;
import com.lumens.scheduler.JobConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class JobExecutor implements Job {

    private final Logger log = LogSysFactory.getLogger(JobExecutor.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDetail job = jec.getJobDetail();
        String projectId = job.getKey().getName();
        String jobId = job.getKey().getGroup();
        String jobName = job.getJobDataMap().getString(JobConstants.JOB_NAME);
        TransformEngine engine = (TransformEngine) job.getJobDataMap().get(JobConstants.ENGINE_OBJECT);
        TransformProject project = (TransformProject) job.getJobDataMap().get(JobConstants.PROJECT_OBJECT);

        log.info(String.format("Start Job [%s:%s] to execute project [%s:%s] ", jobId, jobName, projectId, project.getName()));

        try {
            // Execute all start rules to drive the ws connector
            List<ResultHandler> handlers = new ArrayList<>();
            // TODO
            // handlers.add(new DataElementLoggingHandler(Long.parseLong(projectId), projectName));
            engine.execute(new SequenceTransformExecuteJob(project, handlers));
        } catch (Exception ex) {
            log.error(String.format("Failed on starting Job [%s:%s] to execute project [%s:%s] ", jobId, jobName, projectId, project.getName()));
            throw new JobExecutionException(ex);
        }
    }
}
