/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler;

import com.lumens.scheduler.impl.DefaultScheduler;
import com.lumens.scheduler.impl.DefaultTrigger;
import com.lumens.scheduler.impl.DefaultJob;
import com.lumens.scheduler.SchedulerFactory;
import com.lumens.sysdb.dao.ProjectDAO;
import java.util.Date;
import com.lumens.engine.TransformProject;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.entity.Project;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SchedulerTest     
{
    @Before
    public void Before() {
        ProjectDAO projectDAO = DAOFactory.getProjectDAO();
        long projectId = 1421324074892L;
        if (projectDAO.getProject(projectId) == null){
            projectDAO.create(new Project(projectId, "testPoject", "testDescription",""));  // TODO: add data
        }        
    }

    @Test
    public void SchedulerTest() {
        DefaultScheduler scheduler = (DefaultScheduler)SchedulerFactory.get().createScheduler();
        scheduler.start();        
        TransformProject project = new TransformProject();
        project.setName("Test project");
        JobTrigger trigger = new DefaultTrigger(new Date(System.currentTimeMillis() + 10000), new Date(), 1,1 );
        
        DefaultJob job = new DefaultJob(1001, "job1001", "This is a sample job");
        job.addProject(1421324074892L);
        scheduler.addSchedule(job, trigger);
        scheduler.schedule();
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {       
            throw new RuntimeException(ex);
        }
    }
}