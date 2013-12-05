package com.lumens.connector;

import com.lumens.connector.database.client.oracle.OracleConnector;
import com.lumens.connector.database.DatabaseConstants;
import com.lumens.connector.database.client.oracle.OracleClient;
import com.lumens.connector.database.client.oracle.OracleConstants;
import static com.lumens.connector.database.client.oracle.OracleConstants.FIELDS;
import com.lumens.connector.database.client.oracle.OracleOperation;
import com.lumens.connector.database.client.oracle.sql.OracleQuerySQLBuilder;
import com.lumens.connector.database.client.oracle.sql.OracleWriteSQLBuilder;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.Value;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ConnectorTest extends TestCase implements DatabaseConstants, OracleConstants {

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
        OracleConnector cntr = new OracleConnector();
        try {
            HashMap<String, Value> props = new HashMap<String, Value>();
            props.put(OJDBC, new Value("file:///C:/app/washaofe/product/11.2.0/dbhome/jdbc/lib/ojdbc6.jar"));
            props.put(CONNECTION_URL, new Value("jdbc:oracle:thin:@localhost:1521:orcl"));
            props.put(USER, new Value("hr"));
            props.put(PASSWORD, new Value("hr"));
            props.put(FULL_LOAD, new Value(true));
            cntr.setPropertyList(props);
            cntr.open();
            FileOutputStream fos = new FileOutputStream("C:/db.tables.xml");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (Format format : cntr.getFormatList(null).values()) {
                FormatSerializer xml = new FormatSerializer(format);
                xml.writeToXml(baos);
            }
            //String xmlContent = baos.toString("UTF-8");
            //System.out.println(xmlContent);
            fos.write(baos.toByteArray());
            fos.close();
        } finally {
            cntr.close();
        }
    }

    public void testOracleSQLBuilder() {
        // Test select SQL generating
        Format employeeFmt = new DataFormat("Testtable", Form.STRUCT);
        employeeFmt.addChild(CLAUSE, Form.FIELD);
        Format fields = employeeFmt.addChild(FIELDS, Form.STRUCT);
        fields.addChild(new DataFormat("Id", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("name", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("job_title", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("department", Form.FIELD, Type.STRING));
        OracleQuerySQLBuilder sql = new OracleQuerySQLBuilder(employeeFmt);
        Element employeeQuery = new DataElement(employeeFmt);
        employeeQuery.addChild(CLAUSE).setValue("where Id = 'E10001' order by Id group by department");
        String sqlSelect = sql.generateSelectSQL(employeeQuery);
        System.out.println("Generated select SQL: " + sqlSelect);
        assertTrue("SELECT Id, name, job_title, department FROM Testtable where Id = 'E10001' order by Id group by department".equalsIgnoreCase(sqlSelect));

        // Test insert SQL generating
        Element employee = new DataElement(employeeFmt);
        employee.addChild(CLAUSE).setValue("where Id = 'E10001'");
        Element dfields = employee.addChild(FIELDS);
        dfields.addChild("Id").setValue("E10001");
        dfields.addChild("name").setValue("James");
        dfields.addChild("job_title").setValue("Software engineer");
        dfields.addChild("department").setValue("Software R&D");
        OracleWriteSQLBuilder sqlWrite = new OracleWriteSQLBuilder();
        String sqlInsert = sqlWrite.generateInsertSQL(employee);
        System.out.println("Generated insert SQL: " + sqlInsert);

        // Test update SQL generating
        employee.setValue("where name like 'J%'");
        String sqlUpdate = sqlWrite.generateUpdateSQL(employee);
        System.out.println("Generated update SQL: " + sqlUpdate);
    }

    public void testOracleOperation() throws Exception {
        Format employeeFmt = new DataFormat("EMPLOYEES", Form.STRUCT);
        employeeFmt.addChild(CLAUSE, Form.FIELD);
        employeeFmt.addChild(OPERATION, Form.FIELD);
        Format fields = employeeFmt.addChild(FIELDS, Form.STRUCT);
        fields.addChild(new DataFormat("EMPLOYEE_ID", Form.FIELD, Type.INTEGER));
        fields.addChild(new DataFormat("FIRST_NAME", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("LAST_NAME", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("EMAIL", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("PHONE_NUMBER", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("HIRE_DATE", Form.FIELD, Type.DATE));
        fields.addChild(new DataFormat("JOB_ID", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("SALARY", Form.FIELD, Type.FLOAT));
        fields.addChild(new DataFormat("COMMISSION_PCT", Form.FIELD, Type.INTEGER));
        fields.addChild(new DataFormat("MANAGER_ID", Form.FIELD, Type.INTEGER));
        fields.addChild(new DataFormat("DEPARTMENT_ID", Form.FIELD, Type.INTEGER));

        Format employeeFmtTest = new DataFormat("EMPLOYEES_TEST", Form.STRUCT);
        employeeFmtTest.addChild(CLAUSE, Form.FIELD, Type.STRING);
        employeeFmtTest.addChild(OPERATION, Form.FIELD, Type.STRING);
        Format fields_test = employeeFmtTest.addChild(FIELDS, Form.STRUCT);
        fields_test.addChild(new DataFormat("EMPLOYEE_ID", Form.FIELD, Type.INTEGER));
        fields_test.addChild(new DataFormat("FIRST_NAME", Form.FIELD, Type.STRING));
        fields_test.addChild(new DataFormat("LAST_NAME", Form.FIELD, Type.STRING));
        fields_test.addChild(new DataFormat("EMAIL", Form.FIELD, Type.STRING));
        fields_test.addChild(new DataFormat("PHONE_NUMBER", Form.FIELD, Type.STRING));
        fields_test.addChild(new DataFormat("HIRE_DATE", Form.FIELD, Type.DATE));
        fields_test.addChild(new DataFormat("JOB_ID", Form.FIELD, Type.STRING));
        fields_test.addChild(new DataFormat("SALARY", Form.FIELD, Type.FLOAT));
        fields_test.addChild(new DataFormat("COMMISSION_PCT", Form.FIELD, Type.INTEGER));
        fields_test.addChild(new DataFormat("MANAGER_ID", Form.FIELD, Type.INTEGER));
        fields_test.addChild(new DataFormat("DEPARTMENT_ID", Form.FIELD, Type.INTEGER));

        StringBuilder alterSession = new StringBuilder();
        alterSession.append("alter session set NLS_DATE_FORMAT='yyyy-mm-dd'");
        OracleClient client = new OracleClient("file:///C:/app/washaofe/product/11.2.0/dbhome/jdbc/lib/ojdbc6.jar",
                                               "jdbc:oracle:thin:@localhost:1521:orcl", "hr", "hr", alterSession.toString());
        client.open();

        OracleOperation oo = new OracleOperation(client);
        oo.begin();
        Element select = new DataElement(employeeFmt);
        select.addChild(OPERATION).setValue("select");
        OperationResult result = oo.execute(select, employeeFmt);
        oo.end();

        System.out.println("Got recoreds: " + result.getResult().size());
        TransformRule rule = new TransformRule(employeeFmtTest);
        rule.getRuleItem("operation").setScript("'INSERT'");
        rule.getRuleItem("fields.EMPLOYEE_ID").setScript("@fields.EMPLOYEE_ID");
        rule.getRuleItem("fields.FIRST_NAME").setScript("return @fields.FIRST_NAME + '-test'");
        rule.getRuleItem("fields.LAST_NAME").setScript("return (@fields.LAST_NAME + '-' + dateToString(now(), 'yyyy-MM-dd HH:mm:ss SSS').substr(11))");
        rule.getRuleItem("fields.EMAIL").setScript("@fields.EMAIL");
        rule.getRuleItem("fields.PHONE_NUMBER").setScript("@fields.PHONE_NUMBER");
        rule.getRuleItem("fields.HIRE_DATE").setScript("return dateToString(@fields.HIRE_DATE, 'yyyy-MM-dd')");
        rule.getRuleItem("fields.JOB_ID").setScript("@fields.JOB_ID");
        rule.getRuleItem("fields.SALARY").setScript("@fields.SALARY");
        rule.getRuleItem("fields.COMMISSION_PCT").setScript("@fields.COMMISSION_PCT");
        rule.getRuleItem("fields.MANAGER_ID").setScript("@fields.MANAGER_ID");
        rule.getRuleItem("fields.DEPARTMENT_ID").setScript("@fields.DEPARTMENT_ID");

        Processor transformProcessor = new TransformProcessor();
        List<Element> employeeTest = new ArrayList<Element>();
        for (Element e : result.getResult()) {
            List<Element> resultList = (List<Element>) transformProcessor.execute(rule, e);
            employeeTest.addAll(resultList);
        }
        assertTrue(employeeTest.size() == result.getResult().size());
        OracleWriteSQLBuilder sqlWrite = new OracleWriteSQLBuilder();
        for (Element e : employeeTest) {
            oo.execute(e, null);
        }

        // Ending
        client.close();
    }
}
