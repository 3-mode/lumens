/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.engine.TransformEngine;
import com.lumens.scheduler.impl.DefaultScheduler;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.engine.TransformProject;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.entity.Job;
import com.lumens.sysdb.entity.Project;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SchedulerTest {

    // To run test, you need to have db ready : jdbc:derby://localhost:1527/lumens
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

        JobConfiguration failJob = JobConfigurationBuilder.build(
        new Job(1001, "job1001", "this is a sample job", 1, 1, System.currentTimeMillis(), System.currentTimeMillis() + 10000));
        //failJob.addProject(1111111111111L);
        scheduler.addSchedule(failJob);
        scheduler.saveJob(failJob.getId());
        scheduler.deleteJob(failJob.getId());

        JobConfiguration realJob = JobConfigurationBuilder.build(
        new Job(1001, "job1001", "this is a sample job", 1, 1, System.currentTimeMillis(), System.currentTimeMillis() + 10000));
        //realJob.addProject(1421324074892L);
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
    public void MonitorTest() throws InterruptedException {
        TransformEngine engine = new TransformEngine();
        engine.start("../dist/lumens/addin");
        DefaultScheduler scheduler = (DefaultScheduler) SchedulerFactory.get().createScheduler(engine);
        scheduler.start();
        TransformProject project = new TransformProject();
        project.setName("Test project");

        JobConfiguration realJob = JobConfigurationBuilder.build(
        new Job(1001, "job1001", "this is a sample job", 1, 1, System.currentTimeMillis(), System.currentTimeMillis() + 10000));
        //realJob.addProject(1421324074892L);
        scheduler.addSchedule(realJob);
        scheduler.stopJob(realJob.getId());
        scheduler.startJob(realJob.getId());
        // TODO:Add monitor test
    }
}
