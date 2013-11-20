package com.lumens.connector;

import com.lumens.connector.database.DatabaseConnector;
import com.lumens.connector.database.client.oracle.OracleQuerySQLBuilder;
import com.lumens.connector.database.client.oracle.OracleWriteSQLBuilder;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.connector.webservice.soap.SOAPConstants;
import com.lumens.connector.webservice.soap.SOAPMessageBuilder;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.Value;
import com.lumens.model.serializer.ElementXmlSerializer;
import com.lumens.model.serializer.FormatXmlSerializer;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 * Unit test for simple App.
 */
public class ConnectorTest extends TestCase implements SOAPConstants {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConnectorTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ConnectorTest.class);
    }

    public void testOracleConnector() throws Exception {
        DatabaseConnector cntr = new DatabaseConnector();
        try {

            HashMap<String, Value> props = new HashMap<String, Value>();
            props.put(DatabaseConnector.OJDBC, new Value("file:///C:/app/washaofe/product/11.2.0/dbhome/jdbc/lib/ojdbc6.jar"));
            props.put(DatabaseConnector.CONNECTION_URL, new Value("jdbc:oracle:thin:@localhost:1521:orcl"));
            props.put(DatabaseConnector.USER, new Value("hrcms"));
            props.put(DatabaseConnector.PASSWORD, new Value("hrcms"));
            props.put(DatabaseConnector.FULL_LOAD, new Value(true));
            cntr.setPropertyList(props);
            cntr.open();
            FileOutputStream fos = new FileOutputStream("C:/db.tables.xml");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (Format format : cntr.getFormatList(null).values()) {
                FormatXmlSerializer xml = new FormatXmlSerializer(format);
                xml.write(baos);
            }
            String xmlContent = baos.toString("UTF-8");
            //System.out.println(xmlContent);
            //fos.write(baos.toByteArray());
            fos.close();
            //osw.write(xmlContent);
            //osw.close();
        } finally {
            cntr.close();
        }
    }

    public void testCCS02WebService() throws Exception {
        HashMap<String, Value> props = new HashMap<String, Value>();
        props.put(WebServiceConnector.WSDL, new Value(getClass().getResource("/Symantec_CC/UsernameSecurity_1.wsdl").toString()));
        WebServiceConnector connector = new WebServiceConnector();
        connector.setPropertyList(props);
        connector.open();
        Map<String, Format> consumes = connector.getFormatList(Direction.IN);
        Format searchService = connector.getFormat(consumes.get("Search"), "Search.assetSearchCriteria.Filter.Expression.ExpressionField", Direction.IN);
        Map<String, Format> produces = connector.getFormatList(Direction.OUT);
        Format SearchResultDisplayName = connector.getFormat(produces.get("Search"), "SearchResponse.SearchResult.Asset.DisplayName", Direction.OUT);
        TransformRule rule = new TransformRule(searchService);
        rule.getRuleItem("Search.assetSearchCriteria.Filter.Expression.ExpressionField").setScript("\"displayName\"");
        rule.getRuleItem("Search.assetSearchCriteria.Filter.Expression.ExpressionOperator").setScript("\"EqualTo\"");
        rule.getRuleItem("Search.assetSearchCriteria.Filter.Expression.ExpressionValue").setScript("\"sparta\\\\am01\"");
        Processor transformProcessor = new TransformProcessor();
        List<Element> result = (List<Element>) transformProcessor.execute(rule, null);
        new ElementXmlSerializer(result.get(0), true).write(System.out);
        connector.close();
        SOAPMessageBuilder soapBuilder = new SOAPMessageBuilder();
        SOAPEnvelope envelope = soapBuilder.buildSOAPMessage(result.get(0));
        System.out.println(envelope);
        OMElement o = envelope.getBody();
        o = (OMElement) o.getChildrenWithLocalName("Search").next();
        o = (OMElement) o.getChildrenWithLocalName("assetSearchCriteria").next();
        o = (OMElement) o.getChildrenWithLocalName("Filter").next();
        o = (OMElement) o.getChildrenWithLocalName("Expression").next();
        o = (OMElement) o.getChildrenWithLocalName("ExpressionField").next();
        assertNotNull(o);
    }

    public void TtestWebServiceConnector() throws Exception {
        WebServiceConnector connector = new WebServiceConnector();
        HashMap<String, Value> props = new HashMap<String, Value>();
        props.put(WebServiceConnector.WSDL, new Value(getClass().getResource("/wsdl/IncidentManagement.wsdl").toString()));
        connector.setPropertyList(props);
        connector.open();
        Map<String, Format> services = connector.getFormatList(Direction.IN);
        Format retrieveIncident = services.get("RetrieveIncident");

        connector.getFormat(retrieveIncident, "RetrieveIncidentRequest.model.instance", Direction.IN);
        connector.getFormat(retrieveIncident, "RetrieveIncidentRequest.model.instance.attachments",
                            Direction.IN);
        new FormatXmlSerializer(retrieveIncident).write(System.out);
        assertNotNull(retrieveIncident.getChildByPath("RetrieveIncidentRequest.model.instance.attachments.attachment"));

        TransformRule rule = new TransformRule(retrieveIncident);
        rule.getRuleItem("RetrieveIncidentRequest.attachmentData").setScript("true");
        rule.getRuleItem("RetrieveIncidentRequest.model.instance.AssigneeName").setScript("\'test\'");
        rule.getRuleItem("RetrieveIncidentRequest.model.instance.ClosedTime").setScript("dateFormat(now(), \"yyyy-MM-dd HH:mm:ss\")");
        Processor transformProcessor = new TransformProcessor();
        List<Element> result = (List<Element>) transformProcessor.execute(rule, null);
        new ElementXmlSerializer(result.get(0), true).write(System.out);
        connector.close();
        SOAPMessageBuilder soapBuilder = new SOAPMessageBuilder();
        SOAPEnvelope envelope = soapBuilder.buildSOAPMessage(result.get(0));
        System.out.println(envelope);

        props.put(WebServiceConnector.WSDL, new Value(getClass().getResource("/wsdl/ChinaOpenFundWS.asmx").toString()));
        props.put(WebServiceConnector.PROXY_ADDR, new Value("web-proxy.atl.hp.com"));
        props.put(WebServiceConnector.PROXY_PORT, new Value(8080));
        connector.setPropertyList(props);
        connector.open();
        services = connector.getFormatList(Direction.IN);
        Format getOpenFundString = services.get("getOpenFundString");
        connector.getFormat(getOpenFundString, "getOpenFundString", Direction.IN);
        rule = new TransformRule(getOpenFundString);
        rule.getRuleItem("getOpenFundString.userID").setScript("\"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqq\"");
        transformProcessor = new TransformProcessor();
        result = (List<Element>) transformProcessor.execute(rule, null);
        new ElementXmlSerializer(result.get(0), true).write(System.out);

        services = connector.getFormatList(Direction.OUT);
        getOpenFundString = services.get("getOpenFundString");
        connector.getFormat(getOpenFundString, "getOpenFundStringResponse.getOpenFundStringResult.string.string", Direction.OUT);
        Operation op = connector.getOperation();
        OperationResult opResult = op.execute(result.get(0), getOpenFundString);
        List<Element> response = opResult.getResult();
        new ElementXmlSerializer(response.get(0), true).write(System.out);
        new FormatXmlSerializer(getOpenFundString).write(System.out);

    }

    public void TtestPPMWS() throws Exception {
        String ppmWSDL = "http://16.173.232.74:16800/itg/ppmservices/DemandService?wsdl";
        WebServiceConnector connector = new WebServiceConnector();
        HashMap<String, Value> props = new HashMap<String, Value>();
        props.put(WebServiceConnector.WSDL, new Value(ppmWSDL));
        props.put(DatabaseConnector.USER, new Value("admin"));
        props.put(DatabaseConnector.PASSWORD, new Value("admin"));
        connector.setPropertyList(props);
        connector.open();
        Map<String, Format> services = connector.getFormatList(Direction.IN);
        Format getRequests = services.get("getRequests");
        connector.getFormat(getRequests, "getRequests.requestIds.id", Direction.IN);
        assertNotNull(getRequests.getChildByPath("getRequests.requestIds.id"));
        new FormatXmlSerializer(getRequests).write(System.out);
        TransformRule rule = new TransformRule(getRequests);
        rule.getRuleItem("getRequests.requestIds.id").setScript("\"30392\"");
        TransformProcessor transformProcessor = new TransformProcessor();
        List<Element> result = (List<Element>) transformProcessor.execute(rule, null);
        new ElementXmlSerializer(result.get(0), true).write(System.out);
        Operation op = connector.getOperation();
        services = connector.getFormatList(Direction.OUT);
        getRequests = services.get("getRequests");
        connector.getFormat(getRequests, "getRequestsResponse.return", Direction.OUT);
        connector.getFormat(getRequests, "getRequestsResponse.return.fieldChangeNodes", Direction.OUT);
        connector.getFormat(getRequests, "getRequestsResponse.return.simpleFields", Direction.OUT);
        connector.getFormat(getRequests, "getRequestsResponse.return.simpleFields.stringValue", Direction.OUT);
        new FormatXmlSerializer(getRequests).write(System.out);
        assertNotNull(getRequests.getChildByPath("getRequestsResponse.return.simpleFields.stringValue"));
        //List<Element> response = op.execute(result.get(0), getRequests);
        //new DataElementXmlSerializer(response.get(0), "UTF-8", true).write(System.out);
    }

    public void testOracleQuerySQLBuilder() {
        // Test select SQL generating
        Format employeeFmt = new DataFormat("Testtable", Form.STRUCT);
        employeeFmt.addChild(new DataFormat("Id", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("name", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("job_title", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("department", Form.FIELD, Type.STRING));
        OracleQuerySQLBuilder sql = new OracleQuerySQLBuilder(employeeFmt);
        String sqlSelect = sql.generateSelectSQL(null);
        assertTrue("SELECT t1.Id, t1.name, t1.job_title, t1.department FROM Testtable t1".equalsIgnoreCase(sqlSelect));
        System.out.println("Gerenated select SQL: " + sqlSelect);

        // Test insert SQL generating
        Element employee = new DataElement(employeeFmt);
        employee.addChild("Id").setValue("E10001");
        employee.addChild("name").setValue("James");
        employee.addChild("job_title").setValue("Software engineer");
        employee.addChild("department").setValue("Software R&D");
        OracleWriteSQLBuilder sqlWrite = new OracleWriteSQLBuilder();
        String sqlInsert = sqlWrite.generateInsertSQL(employee);
        System.out.println("Generated insert SQL: " + sqlInsert);

        // Test update SQL generating
        //sqlWrite.generateUpdateSQL(employee);
    }
}
