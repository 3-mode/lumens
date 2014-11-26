package com.lumens.engine;

import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.database.DatabaseConstants;
import com.lumens.connector.webservice.soap.SoapConstants;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.component.instrument.DataTransformator;
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
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class EngineTest extends Assert implements SoapConstants {

    class MyResultHandler implements ResultHandler {

        @Override
        public void process(TransformComponent src, String resultName, List<Element> results) {
            System.out.println(">>>>>>>>>>>Transform>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Component name: " + src.getName() + "; Format name: " + resultName + "; result size: " + results.size());
            try {
                System.out.println("Only print last one");
                new ElementSerializer(results.get(results.size() - 1), true).writeToXml(System.out);
            } catch (Exception ex) {
            }
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    public EngineTest() {
        AddinEngine ae = new AddinEngine(EngineTest.class.getClassLoader());
        ae.start();
        AddinContext ac = ae.getAddinContext();
        new com.lumens.connector.database.client.oracle.service.Activator().start(ac);
        new com.lumens.connector.webservice.service.Activator().start(ac);
        EngineContext.start(new DefaultConnectorFactoryHolder(ac));
    }

    @Test
    public void testEngineWithWebservice() throws Exception {
        int nameCounter = 1;
        // Create ws connector to read data
        HashMap<String, Value> props = new HashMap<>();
        props.put(WSDL, new Value(getClass().getResource("/wsdl/ChinaOpenFundWS.asmx").toString()));
        props.put(PROXY_ADDR, new Value("web-proxy.atl.hp.com"));
        props.put(PROXY_PORT, new Value(8080));
        DataSource datasource = new DataSource("type-soap", "20001");
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
        FormatEntry targetInEntry = datasource.registerFormat(targetName, getOpenFundStringRequest, Direction.IN);
        FormatEntry targetOutEntry = datasource.registerFormat(targetName, getOpenFundStringResponse, Direction.OUT);
        String targetName2 = getOpenFundStringRequest.getName() + (nameCounter++);
        FormatEntry target2InEntry = datasource.registerFormat(targetName2, getOpenFundStringRequest, Direction.IN);
        FormatEntry target2OutEntry = datasource.registerFormat(targetName2, getOpenFundStringResponse, Direction.OUT);
        //******************************************************************************************
        // Create transformation to a data source
        DataTransformator callGetOpenFundString = new DataTransformator("10001");
        callGetOpenFundString.setName("GetOpenFundString-WS-Transform");
        callGetOpenFundString.setDescription("Test DT 1");

        DataTransformator callGetOpenFundString2 = new DataTransformator("10002");

        callGetOpenFundString2.setName("GetOpenFundString2-WS-Transform");
        callGetOpenFundString2.setDescription("Test DT 2");

        // Link transform call to datasource webservice getOpenFundString
        callGetOpenFundString.targetTo(datasource);
        datasource.targetTo(callGetOpenFundString2);
        callGetOpenFundString2.targetTo(datasource);
        // Create start point transformation
        TransformRule rule1 = callGetOpenFundString.registerRule(null, targetInEntry);
        rule1.getRuleItem("getOpenFundString.userID").setScript("'123'");

        // Create the loop transformation datasource->transformation->datasource
        TransformRule rule2 = callGetOpenFundString2.registerRule(targetOutEntry, target2InEntry);
        rule2.getRuleItem("getOpenFundString.userID").setScript("@getOpenFundStringResponse.getOpenFundStringResult.string");

        //*************Test project********************************************
        TransformProject project = new TransformProject();

        project.setName("The demo project");
        project.setDescription("test project description demo");
        List<DataSource> dsList = project.getDatasourceList();
        List<DataTransformator> dtList = project.getDataTransformatorList();
        dsList.add(datasource);
        dtList.add(callGetOpenFundString);
        dtList.add(callGetOpenFundString2);
        TransformProject newProject = doTestProjectSerialize(project);
        // Test SerializeJson
        TransformProject projectReaded = new TransformProject();

        new ProjectSerializer(projectReaded)
        .readFromJson(getResourceAsByteArrayInputStream("/json/project.json"));
        assertTrue(projectReaded.getDatasourceList().size() == 1);
        assertTrue(projectReaded.getDataTransformatorList().size() == 2);

        //**********************************************************************
        // Execute all start rules to drive the ws connector
        List<ResultHandler> handlers = new ArrayList<>();

        handlers.add(new MyResultHandler());
        TransformEngine stEngine = new TransformEngine();

        stEngine.execute(new SingleThreadTransformExecuteJob(newProject, handlers));
        stEngine.execute(new SingleThreadTransformExecuteJob(projectReaded));
        Thread.sleep(10000);
    }

    @Test
    public void testReadingProjectFromXml() throws Exception {

        // Read project and write it again
        TransformProject newProject = new TransformProject();
        ProjectSerializer projXml = new ProjectSerializer(newProject);
        projXml.readFromXml(getResourceAsByteArrayInputStream("/xml/project.xml"));
        List<ResultHandler> handlers = new ArrayList<>();
        handlers.add(new MyResultHandler());
        new SingleThreadTransformExecuteJob(newProject, handlers).run();
        // TODO check project object
    }

    @Test
    public void testOracleConnectorInEngine() throws Exception {
        HashMap<String, Value> props = new HashMap<>();
        props.put(DatabaseConstants.OJDBC, new Value("file:///C:/app/washaofe/product/11.2.0/dbhome/jdbc/lib/ojdbc6.jar"));
        props.put(DatabaseConstants.CONNECTION_URL, new Value("jdbc:oracle:thin:@localhost:1521:xe"));
        props.put(DatabaseConstants.USER, new Value("hr"));
        props.put(DatabaseConstants.PASSWORD, new Value("hr"));
        props.put(DatabaseConstants.SESSION_ALTER, new Value("alter session set NLS_DATE_FORMAT='yyyy-mm-dd'"));
        DataSource datasource = new DataSource("type-oracle-jdbc", "20002");
        datasource.setName("Database HR");
        datasource.setDescription("this is testing demo datasource for oracle jdbc");
        datasource.setPropertyList(props);
        datasource.setX(400);
        datasource.setY(200);
        datasource.open();
        Map<String, Format> inputList = datasource.getFormatList(Direction.IN);
        Connector connector = datasource.getConnector();
        Format inputArg = connector.getFormat(inputList.get("EMPLOYEES_TEST"), "clause", Direction.IN);
        connector.getFormat(inputArg, "operation", Direction.IN);
        Map<String, Format> produces = datasource.getFormatList(Direction.OUT);
        Format returnOut = connector.getFormat(produces.get("EMPLOYEES_TEST"), "fields", Direction.OUT);
        inputArg = inputArg.recursiveClone();
        returnOut = returnOut.recursiveClone();
        FormatEntry inFormatEntry = datasource.registerFormat("sqlSelect", inputArg, Direction.IN);
        FormatEntry outFormatEntry = datasource.registerFormat("sqlSelect", returnOut, Direction.OUT);

        DataSource ws = new DataSource("type-soap", "20003");
        Map<String, Value> wsProps = new HashMap<>();
        wsProps.put(WSDL, new Value("http://webservice.webxml.com.cn/webservices/DomesticAirline.asmx?wsdl"));/*
        wsProps.put(PROXY_ADDR, new Value("web-proxy.atl.hp.com"));
        wsProps.put(PROXY_PORT, new Value(8080));//*/
        ws.setPropertyList(wsProps);
        ws.setName("Airline query soap");
        ws.setDescription("Airline query soap test");
        ws.setX(500);
        ws.setY(100);

        DataTransformator queryEmployeeTestTableTransformator = new DataTransformator("10003");
        queryEmployeeTestTableTransformator.setName("drive the employee test table query");
        queryEmployeeTestTableTransformator.setDescription("drive the employee test table query");
        queryEmployeeTestTableTransformator.setX(100);
        queryEmployeeTestTableTransformator.setY(100);
        queryEmployeeTestTableTransformator.targetTo(datasource);

        TransformRule rule = queryEmployeeTestTableTransformator.registerRule(null, inFormatEntry);
        rule.getRuleItem("operation").setScript("\"select\"");

        TransformProject project = new TransformProject();
        project.setName("demo oracle project");
        project.setDescription("It is used to query empolyee test table all records");
        List<DataSource> dsList = project.getDatasourceList();
        List<DataTransformator> dtList = project.getDataTransformatorList();
        dsList.add(datasource);
        dsList.add(ws);
        dtList.add(queryEmployeeTestTableTransformator);
        TransformProject newProject = doTestProjectSerialize2(project);
        List<ResultHandler> handlers = new ArrayList<>();
        handlers.add(new MyResultHandler());
        TransformEngine stEngine = new TransformEngine();
        stEngine.execute(new SingleThreadTransformExecuteJob(newProject, handlers));
        Thread.sleep(10000);
    }

    private TransformProject doTestProjectSerialize2(TransformProject project) throws Exception {
        ByteArrayOutputStream baosXML = new ByteArrayOutputStream();
        ByteArrayOutputStream baosJson = new ByteArrayOutputStream();
        new ProjectSerializer(project).writeToXml(baosXML);
        new ProjectSerializer(project).writeToJson(baosJson);
        System.out.println(baosXML.toString());
        System.out.println(baosJson.toString());
        String strJson = baosJson.toString();
        System.out.println(strJson.replace("\\n", "\\\\n").replace("\\r", "\\\\r").replace("\\t", "\\\\t").replace("\"", "\\\""));
        System.out.println();
        // Read project and write it again
        TransformProject newProject = new TransformProject();
        ProjectSerializer projXmlJson = new ProjectSerializer(newProject);
        projXmlJson.readFromJson(new ByteArrayInputStream(baosJson.toByteArray()));
        //projXml.write(System.out);
        return newProject;
    }

    private TransformProject doTestProjectSerialize(TransformProject project) throws Exception {
        ByteArrayOutputStream baosXML = new ByteArrayOutputStream();
        ByteArrayOutputStream baosJson = new ByteArrayOutputStream();
        new ProjectSerializer(project).writeToXml(baosXML);
        new ProjectSerializer(project).writeToJson(baosJson);
        System.out.println(baosXML.toString());
        System.out.println(baosJson.toString());
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
}