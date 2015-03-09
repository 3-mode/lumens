/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.engine.TransformEngine;
import com.lumens.scheduler.impl.DefaultScheduler;
import com.lumens.scheduler.impl.DefaultJobBuilder;
import com.lumens.sysdb.dao.ProjectDAO;
import java.util.Date;
import com.lumens.engine.TransformProject;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.entity.Project;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SchedulerTest {

    @Before
    public void Before() {
        ProjectDAO projectDAO = DAOFactory.getProjectDAO();
        long projectId = 1421324074892L;
        if (projectDAO.getProject(projectId) == null) {
            projectDAO.create(new Project(projectId, "testPoject", "testDescription", ""));  // TODO: add data
        }

        long projectId2 = 1111111111111L;
        if (projectDAO.getProject(projectId2) == null) {
            projectDAO.create(new Project(projectId2, "testPoject", "testDescription", ""));  // TODO: add data
        }
    }

    @Test
    public void SchedulerTest() {
        TransformEngine engine = new TransformEngine();
        engine.start("../dist/lumens/addin");
        DefaultScheduler scheduler = (DefaultScheduler) SchedulerFactory.get().createScheduler(engine);
        scheduler.start();
        TransformProject project = new TransformProject();
        project.setName("Test project");

        Job failJob = new DefaultJobBuilder()
                .withJobId(1001)
                .withJobName("job1001")
                .withJobDescription("This is a sample job")
                .withStartTime(System.currentTimeMillis())
                .withEndTime(System.currentTimeMillis() + 10000)
                .withRepeat(1)
                .withInterval(1)
                .build();
        failJob.addProject(1111111111111L);
        scheduler.addSchedule(failJob);
        scheduler.saveJob(failJob.getId());
        scheduler.deleteJob(failJob.getId());

        Job realJob = new DefaultJobBuilder()
                .withJobId(1001)
                .withJobName("job1001")
                .withJobDescription("This is a sample job")
                .withStartTime(System.currentTimeMillis())
                .withEndTime(System.currentTimeMillis() + 10000)
                .withRepeat(1)
                .withInterval(1)
                .build();
        realJob.addProject(1421324074892L);
        scheduler.addSchedule(realJob);
        scheduler.stopJob(realJob.getId());
        scheduler.startJob(realJob.getId());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void MonitorTest() {
        TransformEngine engine = new TransformEngine();
        engine.start("../dist/lumens/addin");
        DefaultScheduler scheduler = (DefaultScheduler) SchedulerFactory.get().createScheduler(engine);
        scheduler.start();
        TransformProject project = new TransformProject();
        project.setName("Test project");

        Job realJob = new DefaultJobBuilder()
                .withJobId(1001)
                .withJobName("job1001")
                .withJobDescription("This is a sample job")
                .withStartTime(System.currentTimeMillis())
                .withEndTime(System.currentTimeMillis() + 10000)
                .withRepeat(1)
                .withInterval(1)                
                .build();
        realJob.addProject(1421324074892L);
        scheduler.addSchedule(realJob);
        scheduler.stopJob(realJob.getId());
        scheduler.startJob(realJob.getId());

        // TODO:Add monitor test
    }
}
