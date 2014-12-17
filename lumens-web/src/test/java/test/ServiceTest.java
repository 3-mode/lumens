/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package test;

import com.lumens.backend.ApplicationContext;
import com.lumens.backend.sql.DAOFactory;
import com.lumens.backend.sql.dao.ProjectDAO;
import com.lumens.backend.sql.entity.Project;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.handler.TransformerResultHandler;
import com.lumens.engine.run.SingleThreadTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void testDBProject() throws Exception {
        if (true) {
            ApplicationContext.createInstance(ServiceTest.class.getClassLoader());
            ProjectDAO pDAO = DAOFactory.getProjectDAO();
            Project project = pDAO.getProject(1415415434544L);
            TransformProject projectInstance = new TransformProject();
            new ProjectSerializer(projectInstance).readFromJson(new ByteArrayInputStream(project.data.getBytes()));
            //assertEquals(3, projectInstance.getDataTransformerList().size());
            //assertEquals(4, projectInstance.getDatasourceList().size());
            class MyResultHandler implements TransformerResultHandler {

                @Override
                public void process(TransformComponent src, String resultName, List<Element> results) {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        new ElementSerializer(results.get(0), true).writeToJson(baos);
                        System.out.println(baos.toString());
                    } catch (Exception ex) {
                    }
                }
            }
            List<ResultHandler> handlers = new ArrayList<>();
            handlers.add(new MyResultHandler());
            new SingleThreadTransformExecuteJob(projectInstance, handlers).run();
            //ApplicationContext.get().getTransformEngine().execute(new SingleThreadTransformExecuteJob(projectInstance, handlers));
        }
    }

    public void testDeleteProject() {
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        pDAO.delete(1414288134865L);
    }
}
