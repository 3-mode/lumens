/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

import com.lumens.model.DateTime;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.InOutLogDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.entity.InOutLogItem;
import com.lumens.sysdb.entity.Job;
import java.sql.Timestamp;
import java.text.DateFormat;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class SysdbTest {

    @Test
    public void testDeleteProject() {
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        pDAO.delete(0L);
    }

    @Test
    public void testDeleteJob() {
        JobDAO pDAO = DAOFactory.getJobDAO();
        pDAO.delete(0L);
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
