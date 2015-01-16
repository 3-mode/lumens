/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package test;

import com.lumens.backend.ApplicationContext;
import com.lumens.backend.DataElementLoggingHandler;
import com.lumens.backend.service.ProjectService;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.InOutLogDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.entity.InOutLogItem;
import com.lumens.sysdb.entity.Project;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.handler.TransformerResultHandler;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.model.DateTime;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void testDBProject() throws Exception {
        if (true) {
            ApplicationContext.createInstance(ServiceTest.class.getClassLoader());
            ProjectDAO pDAO = DAOFactory.getProjectDAO();
            Project project = pDAO.getProject(1421324074892L); //1421234160179L page //1415415434544L //1421324074892L CSV
            TransformProject projectInstance = new TransformProject();
            new ProjectSerializer(projectInstance).readFromJson(new ByteArrayInputStream(project.data.getBytes()));
            //assertEquals(3, projectInstance.getDataTransformerList().size());
            //assertEquals(4, projectInstance.getDatasourceList().size());
            class MyResultHandler implements TransformerResultHandler {
                @Override
                public void processOutput(TransformComponent src, String targetName, List<Element> output) {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        new ElementSerializer(output.get(0), true).writeToJson(baos);
                        System.out.println(baos.toString());
                    } catch (Exception ex) {
                    }
                }

                @Override
                public void processInput(TransformComponent src, String targetName, List<Element> input) {
                }
            }
            List<ResultHandler> handlers = new ArrayList<>();
            handlers.addAll(Arrays.asList(new DataElementLoggingHandler(project.id, project.name)));
            new SequenceTransformExecuteJob(projectInstance, handlers).run();
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

}
