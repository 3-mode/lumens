/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.engine.TransformEngine;
import com.lumens.scheduler.impl.DefaultScheduler;
import com.lumens.scheduler.impl.DefaultTrigger;
import com.lumens.scheduler.impl.DefaultJob;
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
        JobTrigger trigger = new DefaultTrigger(System.currentTimeMillis(), System.currentTimeMillis() + 10000, 1, 1);

        DefaultJob failJob = new DefaultJob(1001, "job1001", "This is a sample job");
        failJob.addProject(1111111111111L);
        scheduler.addSchedule(failJob, trigger);
        scheduler.saveJob(failJob.getId());
        scheduler.deleteJob(failJob.getId());

        DefaultJob realJob = new DefaultJob(1001, "job1001", "This is a sample job");
        realJob.addProject(1421324074892L);
        scheduler.addSchedule(realJob, trigger);
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
        JobTrigger trigger = new DefaultTrigger(System.currentTimeMillis(), System.currentTimeMillis() + 10000, 1, 1);

        DefaultJob realJob = new DefaultJob(1001, "job1001", "This is a sample job");
        realJob.addProject(1421324074892L);
        scheduler.addSchedule(realJob, trigger);
        scheduler.stopJob(realJob.getId());
        scheduler.startJob(realJob.getId());
        
        // Add monitor test
    }
}
