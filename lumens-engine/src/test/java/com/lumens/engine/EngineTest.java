package com.lumens.engine;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.engine.component.DataSource;
import com.lumens.engine.component.DataTransformation;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.engine.run.Executor;
import com.lumens.engine.run.SingleThreadExecuteStack;
import com.lumens.engine.run.TransformExecutor;
import com.lumens.engine.serializer.ProjectXmlSerializer;
import com.lumens.model.Format;
import com.lumens.model.Value;
import com.lumens.processor.transform.TransformRule;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author washaofe
 */
public class EngineTest extends TestCase {
    public EngineTest(String testName) {
        super(testName);
    }

    public void TtestEngine1() throws Exception {
        int nameCounter = 1;
        // Create ws connector to read data
        HashMap<String, Value> props = new HashMap<String, Value>();
        props.put(WebServiceConnector.WSDL, new Value(getClass().getResource("/wsdl/ChinaOpenFundWS.asmx").toString()));
        //props.put(WebServiceConnector.PROXY_ADDR, new Value("web-proxy.atl.hp.com"));
        //props.put(WebServiceConnector.PROXY_PORT, new Value(8080));
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
        Format getOpenFundStringResponse = connector.getFormat(produces.get("getOpenFundString"), "getOpenFundStringResponse.getOpenFundStringResult.string", Direction.OUT);
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
        DataTransformation callGetOpenFundString = new DataTransformation();
        callGetOpenFundString.setName("GetOpenFundString-WS-Transform");
        callGetOpenFundString.setDescription("Test DT 1");

        DataTransformation callGetOpenFundString2 = new DataTransformation();
        callGetOpenFundString2.setName("GetOpenFundString2-WS-Transform");
        callGetOpenFundString2.setDescription("Test DT 2");

        // Link transform call to datasource webservice getOpenFundString
        callGetOpenFundString.targetTo(datasource);
        datasource.targetTo(callGetOpenFundString2);
        callGetOpenFundString2.targetTo(datasource);

        // Create start point transformation
        String startPoint = "DataDriven";
        TransformRule rule1 = new TransformRule(getOpenFundStringRequest);
        rule1.getRuleItem("getOpenFundString.userID").setScript("\"123\"");
        callGetOpenFundString.registerRule(new TransformRuleEntry(startPoint, targetName, rule1));

        // Create the loop transformation datasource->transformation->datasource
        TransformRule rule2 = new TransformRule(getOpenFundStringRequest);
        rule2.getRuleItem("getOpenFundString.userID").setScript("@getOpenFundStringResponse.getOpenFundStringResult.string");
        callGetOpenFundString2.registerRule(new TransformRuleEntry(targetName, targetName2, rule2));

        //*************Test project********************************************
        TransformProject project = new TransformProject();
        project.setName("The demo project");
        project.setDescription("test project description demo");
        List<DataSource> dsList = project.getDatasourceList();
        List<DataTransformation> dtList = project.getDataTransformationList();
        dsList.add(datasource);
        dtList.add(callGetOpenFundString);
        dtList.add(callGetOpenFundString2);
        doTestProjectSerialize(project);
        //**********************************************************************
        // Execute all start rules to drive the ws connector
        SingleThreadExecuteStack executorStack = new SingleThreadExecuteStack();
        executorStack.push(new TransformExecutor(callGetOpenFundString, new TransformExecuteContext(null, startPoint)));
        while (!executorStack.isEmpty()) {
            Executor executor = executorStack.pop();
            List<Executor> tExList = executor.execute();
            executorStack.push(tExList);
        }
    }

    private void doTestProjectSerialize(TransformProject project) throws Exception {
        new ProjectXmlSerializer(project).write(System.out);

        InputStream in = null;
        try {
            in = EngineTest.class.getResourceAsStream("/xml/project.xml");
            // Read project and write it again
            TransformProject newProject = new TransformProject();
            ProjectXmlSerializer projXml = new ProjectXmlSerializer(newProject);
            projXml.read(in);
            projXml.write(System.out);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public void testEmpty() {
        assertTrue(true);
    }
}
