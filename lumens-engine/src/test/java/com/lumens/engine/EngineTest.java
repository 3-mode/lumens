package com.lumens.engine;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.component.instrument.DataTransformator;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.engine.run.ResultHandler;
import com.lumens.engine.run.SingleThreadTransformExecuteJob;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.transform.TransformRule;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.apache.commons.io.IOUtils;

public class EngineTest extends TestCase {

    public EngineTest(String testName) {
        super(testName);
    }

    public void testEngine1() throws Exception {
        int nameCounter = 1;
        // Create ws connector to read data
        HashMap<String, Value> props = new HashMap<>();
        props.put(WebServiceConnector.WSDL, new Value(getClass().getResource("/wsdl/ChinaOpenFundWS.asmx").toString()));
        props.put(WebServiceConnector.PROXY_ADDR, new Value("web-proxy.atl.hp.com"));
        props.put(WebServiceConnector.PROXY_PORT, new Value(8080));
        DataSource datasource = new DataSource(WebServiceConnector.class.getName());
        datasource.setName("ChinaMobile-WebService-SOAP");
        datasource.setPropertyList(props);
        datasource.open();
        datasource.setDescription("this is testing demo datasource for web service");

        // Expand format tree
        //*******************************************************************************************
        Map<String, Format> consumes = datasource.getFormatList(Direction.IN);
        Connector connector = datasource.getConnector();
        Format getOpenFundStringRequest = connector.getFormat(consumes.get("getOpenFundString"), "getOpenFundString.userID", Direction.IN);
        Map<String, Format> produces = datasource.getFormatList(Direction.OUT);
        Format getOpenFundStringResponse = connector.getFormat(produces.get("getOpenFundString"), "getOpenFundStringResponse", Direction.OUT);
        new FormatSerializer(getOpenFundStringRequest).writeToXml(System.out);
        new FormatSerializer(getOpenFundStringResponse).writeToXml(System.out);
        new FormatSerializer(getOpenFundStringResponse).writeToJson(System.out);

        Format getOpenFundStringResponse2 = connector.getFormat(produces.get("getOpenFundDataSet"), "getOpenFundDataSetResponse", Direction.OUT);
        assertNotNull(getOpenFundStringResponse2.getChild("getOpenFundDataSetResponse").getChild("getOpenFundDataSetResult"));

        // Register format
        String targetName = getOpenFundStringRequest.getName() + (nameCounter++);
        // The code is used to create a format copy for registered request
        getOpenFundStringRequest = getOpenFundStringRequest.recursiveClone();
        getOpenFundStringResponse = getOpenFundStringResponse.recursiveClone();
        datasource.registerFormat(targetName, getOpenFundStringRequest, Direction.IN);
        datasource.registerFormat(targetName, getOpenFundStringResponse, Direction.OUT);

        String targetName2 = getOpenFundStringRequest.getName() + (nameCounter++);
        datasource.registerFormat(targetName2, getOpenFundStringRequest, Direction.IN);
        datasource.registerFormat(targetName2, getOpenFundStringResponse, Direction.OUT);

        //******************************************************************************************
        // Create transformation to a data source
        DataTransformator callGetOpenFundString = new DataTransformator();
        callGetOpenFundString.setName("GetOpenFundString-WS-Transform");
        callGetOpenFundString.setDescription("Test DT 1");

        DataTransformator callGetOpenFundString2 = new DataTransformator();
        callGetOpenFundString2.setName("GetOpenFundString2-WS-Transform");
        callGetOpenFundString2.setDescription("Test DT 2");

        // Link transform call to datasource webservice getOpenFundString
        callGetOpenFundString.targetTo(datasource);
        datasource.targetTo(callGetOpenFundString2);
        callGetOpenFundString2.targetTo(datasource);

        // Create start point transformation
        String startPoint = "startWS";
        TransformRule rule1 = new TransformRule(getOpenFundStringRequest);
        rule1.getRuleItem("getOpenFundString.userID").setScript("'123'");
        callGetOpenFundString.registerRule(new TransformRuleEntry(startPoint, targetName, rule1));

        // Create the loop transformation datasource->transformation->datasource
        TransformRule rule2 = new TransformRule(getOpenFundStringRequest);
        rule2.getRuleItem("getOpenFundString.userID").setScript("@getOpenFundStringResponse.getOpenFundStringResult.string");
        callGetOpenFundString2.registerRule(new TransformRuleEntry(targetName, targetName2, rule2));

        //*************Test project********************************************
        TransformProject project = new TransformProject();
        project.setName("The demo project");
        project.setDescription("test project description demo");
        project.getStartEntryList().add(new StartEntry("startWS", callGetOpenFundString));
        List<DataSource> dsList = project.getDatasourceList();
        List<DataTransformator> dtList = project.getDataTransformatorList();
        dsList.add(datasource);
        dtList.add(callGetOpenFundString);
        dtList.add(callGetOpenFundString2);
        TransformProject newProject = doTestProjectSerialize(project);

        // Test SerializeJson
        TransformProject projectReaded = new TransformProject();
        new ProjectSerializer(projectReaded).readFromJson(getResourceAsByteArrayInputStream("/json/project.json"));
        assertTrue(projectReaded.getDatasourceList().size() == 1);
        assertTrue(projectReaded.getDataTransformatorList().size() == 2);

        //**********************************************************************
        // Execute all start rules to drive the ws connector
        class MyResultHandler implements ResultHandler {

            @Override
            public void process(TransformComponent src, String resultName, List<Element> results) {
                System.out.println(">>>>>>>>>>>Transform>>>>>>>>>>>>>>>>>>>>");
                System.out.println("Component name: " + src.getName() + "; Format name: " + resultName + "; result size: " + results.size());
                try {
                    new ElementSerializer(results.get(0), true).writeToXml(System.out);
                } catch (Exception ex) {
                }
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        }
        List<ResultHandler> handlers = new ArrayList<>();
        handlers.add(new MyResultHandler());
        TransformEngine stEngine = new TransformEngine();
        stEngine.execute(new SingleThreadTransformExecuteJob(newProject, handlers));
        stEngine.execute(new SingleThreadTransformExecuteJob(projectReaded));
        Thread.sleep(10000);
    }

    private TransformProject doTestProjectSerialize(TransformProject project) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ProjectSerializer(project).writeToXml(System.out);
        new ProjectSerializer(project).writeToJson(System.out);
        System.out.println();
        // Read project and write it again
        TransformProject newProject = new TransformProject();
        ProjectSerializer projXml = new ProjectSerializer(newProject);
        projXml.readFromXml(getResourceAsByteArrayInputStream("/xml/project.xml"));
        // TODO check project object

        //projXml.write(System.out);
        return newProject;
    }

    public InputStream getResourceAsByteArrayInputStream(String url) throws IOException {
        try (InputStream in = EngineTest.class.getResourceAsStream(url)) {
            return new ByteArrayInputStream(IOUtils.toByteArray(in));
        }
    }

    public void testEmpty() {
        assertTrue(true);
    }
}
