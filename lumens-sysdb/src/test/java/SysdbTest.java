/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

import com.lumens.model.DateTime;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.InOutLogDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.dao.JobProjectRelationDAO;
import com.lumens.sysdb.entity.InOutLogItem;
import com.lumens.sysdb.entity.Job;
import com.lumens.sysdb.entity.JobProjectRelation;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SysdbTest {

    @Before
    public void testCreateJob() {
        Job dbJob = new Job(1234, "firstJob", "This is a test job.", 1, 1, System.currentTimeMillis(),System.currentTimeMillis() + 2000);
        JobDAO pDAO = DAOFactory.getJobDAO();
        JobProjectRelationDAO projectRelation = DAOFactory.getRelationDAO();
        Job job = pDAO.getJob(1234);
        if (job != null) {
            pDAO.delete(job.id);            
        }
        pDAO.create(dbJob);
        projectRelation.deleteAllRelation(1234);
        projectRelation.create(1234, 1111);
        projectRelation.create(1234, 2222);
        projectRelation.create(1234, 3333);
    }

    @Test
    public void testGetJob() {
        JobDAO pDAO = DAOFactory.getJobDAO();
        Job job = pDAO.getJob(1234);
        System.out.println("A test Job details: ");
        System.out.println("  id = " + job.id);
        System.out.println("  name = " + job.name);
        System.out.println("  inteveral = " + job.interval);
        System.out.println("  repeat count = " + job.repeat);
        System.out.println("  start time = " + job.startTime.toString());
        System.out.println("  end time = " + job.endTime.toString());
        System.out.println();
        assertNotNull(job);

        JobProjectRelationDAO projectRelation = DAOFactory.getRelationDAO();
        List<JobProjectRelation> list = projectRelation.getAllRelation(job.id);
        System.out.println("Jobs' Project list:");
        for (JobProjectRelation relation : list) {
            System.out.println("Project id = " + relation.projectId);
        }
    }

    @Test
    public void testGetAllJob() {
        JobDAO pDAO = DAOFactory.getJobDAO();
        List<Job> all = pDAO.getAllJob();
        System.out.println("All Jobs details: ");
        for (Job job : all) {
            System.out.println("  job id = " + job.id);
            System.out.println("  name = " + job.name);
            System.out.println("  inteveral = " + job.interval);
            System.out.println("  repeat count = " + job.repeat);
            System.out.println("  start time = " + job.startTime.toString());
            System.out.println("  end time = " + job.endTime.toString());            

            JobProjectRelationDAO projectRelation = DAOFactory.getRelationDAO();
            List<JobProjectRelation> list = projectRelation.getAllRelation(job.id);
            System.out.println("Jobs' Project list:");
            for (JobProjectRelation relation : list) {
                System.out.println("Project id = " + relation.projectId);
            }
            System.out.println();
        }
        assertTrue(all.size() > 0);
    }

    @After
    public void testDeleteJob() {
        JobDAO pDAO = DAOFactory.getJobDAO();
        pDAO.delete(1234);
        JobProjectRelationDAO projectRelation = DAOFactory.getRelationDAO();
        projectRelation.deleteAllRelation(1234);
    }

    @After
    public void testDeleteProject() {
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        pDAO.delete(1234);
    }

    @Test
    public void testPutLog() {
        InOutLogDAO inoutLogDAO = DAOFactory.getInOutLogDAO();
        InOutLogItem item = new InOutLogItem();
        item.logID = System.currentTimeMillis();
        item.componentID = 100;
        item.componentName = "TestComponent";
        item.projectID = 1000;
        item.targetName = "TestTarget";
        item.direction = "IN";
        item.targetName = "test";
        item.projectID = 1234;
        item.projectName = "project1";
        item.data = "test";
        DateFormat sf = DateTime.DATETIME_PATTERN[0];
        item.lastModifTime = new Timestamp(System.currentTimeMillis());
        inoutLogDAO.create(item);
    }
}
