/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformEngine;
import com.lumens.engine.TransformProject;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.logsys.LogSysFactory;
import com.lumens.scheduler.JobConfiguration;
import com.lumens.scheduler.JobConstants;
import java.util.Iterator;
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
        JobConfiguration jobConfig = (JobConfiguration) job.getJobDataMap().get(JobConstants.JOB_CONFIG);
        TransformEngine engine = (TransformEngine) job.getJobDataMap().get(JobConstants.TRNASFORM_ENGINE);
        String jobId = Long.toString(jobConfig.getId());
        String jobName = jobConfig.getName();

        log.info(String.format("**** Start Job [%s:%s] ", jobId, jobName));
        Iterator<TransformProject> itr = jobConfig.getProjectList().iterator();
        TransformProject project = null;
        try {
            while (itr.hasNext()) {
                project = itr.next();
                engine.execute(new SequenceTransformExecuteJob(project, jobConfig.getInspectionHandlers()));
            }
        } catch (Exception ex) {
            if (project != null)
                log.error(String.format("Failed on starting Job [%s:%s] to execute project [%s] ", jobId, jobName, project.getName()));
            log.error(ex);
            throw new JobExecutionException(ex);
        } finally {
            log.info(String.format("**** Complete Job [%s:%s]", jobId, jobName));
        }
    }
}
