package com.lumens.connector;

import com.lumens.connector.database.DatabaseConnector;
import com.lumens.connector.webservice.WebServiceConnector;
import com.lumens.connector.webservice.soap.SOAPConstants;
import com.lumens.connector.webservice.soap.SOAPMessageBuilder;
import com.lumens.model.Element;
import com.lumens.model.Format;
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
            props.put(DatabaseConnector.OJDBC, new Value("file:///C:/app/washaofe/product/11.2.0/dbhome_1/jdbc/lib/ojdbc6.jar"));
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
            System.out.println(xmlContent);
            fos.write(baos.toByteArray());
            fos.close();
            //osw.write(xmlContent);
            //osw.close();
        } finally {
            cntr.close();
        }
    }

    public void TtestWebServiceConnector() throws Exception {
        WebServiceConnector connector = new WebServiceConnector();
        HashMap<String, Value> props = new HashMap<String, Value>();
        props.put(WebServiceConnector.WSDL, new Value(getClass().getResource(
        "/wsdl/IncidentManagement.wsdl").toString()));
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

        props.put(WebServiceConnector.WSDL, new Value(getClass().getResource(
        "/wsdl/ChinaOpenFundWS.asmx").toString()));
        //props.put(WebServiceConnector.PROXY_ADDR, new Value("web-proxy.atl.hp.com"));
        //props.put(WebServiceConnector.PROXY_PORT, new Value(8080));
        connector.setPropertyList(props);
        connector.open();
        services = connector.getFormatList(Direction.IN);
        Format getOpenFundString = services.get("getOpenFundString");
        connector.getFormat(getOpenFundString, "getOpenFundString", Direction.IN);
        rule = new TransformRule(getOpenFundString);
        rule.getRuleItem("getOpenFundString.userID").setScript(
        "\"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqq\"");
        transformProcessor = new TransformProcessor();
        result = (List<Element>) transformProcessor.execute(rule, null);
        new ElementXmlSerializer(result.get(0), true).write(System.out);

        services = connector.getFormatList(Direction.OUT);
        getOpenFundString = services.get("getOpenFundString");
        connector.getFormat(getOpenFundString,
        "getOpenFundStringResponse.getOpenFundStringResult.string.string",
        Direction.OUT);
        Operation op = connector.getOperation();
        OperationResult opResult = op.execute(result.get(0));
        List<Element> response = opResult.getResult(getOpenFundString);
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
        connector.getFormat(getRequests, "getRequests.requestIds.id",
        Direction.IN);
        assertNotNull(getRequests.getChildByPath("getRequests.requestIds.id"));
        new FormatXmlSerializer(getRequests).write(System.out);
        TransformRule rule = new TransformRule(getRequests);
        rule.getRuleItem("getRequests.requestIds.id").setScript("\"30392\"");
        TransformProcessor transformProcessor = new TransformProcessor();
        List<Element> result = (List<Element>) transformProcessor.execute(rule,
        null);
        new ElementXmlSerializer(result.get(0), true).write(System.out);
        Operation op = connector.getOperation();
        services = connector.getFormatList(Direction.OUT);
        getRequests = services.get("getRequests");
        connector.getFormat(getRequests, "getRequestsResponse.return",
        Direction.OUT);
        connector.getFormat(getRequests,
        "getRequestsResponse.return.fieldChangeNodes",
        Direction.OUT);
        connector.getFormat(getRequests,
        "getRequestsResponse.return.simpleFields",
        Direction.OUT);
        connector.getFormat(getRequests,
        "getRequestsResponse.return.simpleFields.stringValue",
        Direction.OUT);
        new FormatXmlSerializer(getRequests).write(System.out);
        assertNotNull(getRequests.getChildByPath(
        "getRequestsResponse.return.simpleFields.stringValue"));
        //List<Element> response = op.execute(result.get(0), getRequests);
        //new DataElementXmlSerializer(response.get(0), "UTF-8", true).write(System.out);
    }
}
