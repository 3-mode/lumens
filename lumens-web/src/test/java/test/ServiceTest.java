/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package test;

import com.lumens.backend.ApplicationContext;
import com.lumens.backend.service.LogService;
import com.lumens.backend.service.ProjectService;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.InOutLogDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.entity.InOutLogItem;
import com.lumens.sysdb.entity.Project;
import com.lumens.engine.TransformProject;
import com.lumens.engine.handler.InspectionHandler;
import com.lumens.engine.log.TransformComponentInOutLogHandler;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.model.DateTime;
import com.lumens.scheduler.JobConfigurationBuilder;
import com.lumens.scheduler.JobConfiguration;
import com.lumens.sysdb.dao.JobDAO;
import com.lumens.sysdb.entity.Job;
import com.lumens.engine.DBHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.Test;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ServiceTest {

    public ServiceTest() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    public void testDBProject(long projectID) throws Exception {
        if (true) {
            System.setProperty("lumens.base", "../dist/lumens");
            ApplicationContext.createInstance(ServiceTest.class.getClassLoader());
            ProjectDAO pDAO = DAOFactory.getProjectDAO();
            Project project = pDAO.getProject(projectID); //1421234160179L page //1415415434544L //1421324074892L CSV
            TransformProject projectInstance = new TransformProject();
            new ProjectSerializer(projectInstance).readFromJson(new ByteArrayInputStream(project.data.getBytes()));
            //assertEquals(3, projectInstance.getDataTransformerList().size());
            //assertEquals(4, projectInstance.getDatasourceList().size());
            List<InspectionHandler> handlers = new ArrayList<>();
            handlers.addAll(Arrays.asList(new TransformComponentInOutLogHandler()));
            for (int i = 0; i < 100; ++i)
                new SequenceTransformExecuteJob(projectInstance, handlers).execute();
            //ApplicationContext.get().getTransformEngine().execute(new SequenceTransformExecuteJob(projectInstance, handlers));
        }
    }

    public void testDeleteProject() {
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        pDAO.delete(0L);
    }

    public void testPutLog() {
        InOutLogDAO inoutLogDAO = DAOFactory.getInOutLogDAO();
        InOutLogItem item = new InOutLogItem();
        item.logID = System.currentTimeMillis();
        item.componentID = 100;
        item.projectID = 1000;
        item.direction = "IN";
        item.targetName = "test";
        item.data = "test";
        DateFormat sf = DateTime.DATETIME_PATTERN[0];
        item.lastModifTime = new Timestamp(System.currentTimeMillis());
        inoutLogDAO.create(item);
    }

    public void readLog() throws IOException {
        ProjectService service = new ProjectService();
        Response resp = service.getProjectExecutionResults(1415415434544L, 1415415407248L);
        System.out.println(resp.getEntity().toString());
    }

    public void testMicrosecond() throws Exception {
        System.out.println(new Timestamp(System.currentTimeMillis()).toString());
    }

    public void testDateStringToTimeStamp() throws Exception {
        String date = "2015-02-26 22:55";
        Timestamp ts = new Timestamp(DateTime.DATETIME_PATTERN[4].parse(date).getTime());
        System.out.println(ts.toString());
        System.out.println(DateTime.DATETIME_PATTERN[4].format(new Date(ts.getTime())));
    }

    public void testUpdateJob() throws Exception {
        JobDAO jobDAO = DAOFactory.getJobDAO();
        Job job = new Job(1424960575768L,
                          "test",
                          "Test",
                          1,
                          12,
                          DateTime.parse("2015-02-26 22:45").getTime(), 0L);
        long saveId = jobDAO.update(job);
    }

    public void testScheduler() throws Exception {
        System.setProperty("lumens.base", "../dist/lumens");
        ApplicationContext.createInstance(ServiceTest.class.getClassLoader());
        long lJobId = 1425107299173L;
        JobDAO jDAO = DAOFactory.getJobDAO();
        Job job = jDAO.getJob(lJobId);
        JobConfiguration jc = JobConfigurationBuilder.build(job);
        jc.addProject(DBHelper.loadTransformProjectFromDb(lJobId));
        ApplicationContext.get().getScheduler().addSchedule(jc);
        ApplicationContext.get().getScheduler().startJob(lJobId);
        System.out.println("Job started");
        System.in.read();
    }

    public void testLog() {
        System.setProperty("lumens.base", "../dist/lumens");
        ApplicationContext.createInstance(ServiceTest.class.getClassLoader());
        LogService ls = new LogService();
        Response resp = ls.listLogItem(0, 10);
        String str = resp.getEntity().toString();
        System.out.println("logs:" + str);
    }

    public void testCSVProjectMemoryLeak() throws Exception {
        testDBProject(1421842012147L);
    }

}
