package com.lumens.connector;

import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.addin.ServiceEntity;
import com.lumens.connector.database.DatabaseConstants;
import com.lumens.connector.database.client.oracle.OracleClient;
import com.lumens.connector.database.client.oracle.OracleConnectorFactory;
import com.lumens.connector.database.client.oracle.OracleConstants;
import static com.lumens.connector.database.client.oracle.OracleConstants.SQLPARAMS;
import com.lumens.connector.database.client.oracle.OracleOperation;
import com.lumens.connector.database.client.oracle.service.Activator;
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
import com.lumens.processor.transform.TransformMapper;
import com.lumens.processor.transform.TransformRule;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ConnectorTest implements DatabaseConstants, OracleConstants {

    public ConnectorTest() {
    }

    @Test
    public void testEmpty() {
    }

    public void testOracleConnector() throws Exception {
        Connector cntr = new OracleConnectorFactory().createConnector();
        try {
            HashMap<String, Value> props = new HashMap<>();
            props.put(OJDBC, new Value("file:///C:/app/washaofe/product/11.2.0/dbhome/jdbc/lib/ojdbc6.jar"));
            props.put(CONNECTION_URL, new Value("jdbc:oracle:thin:@localhost:1521:xe"));
            props.put(USER, new Value("hr"));
            props.put(PASSWORD, new Value("hr"));
            props.put(FULL_LOAD, new Value(true));
            cntr.setPropertyList(props);
            cntr.open();
            try (FileOutputStream fos = new FileOutputStream("C:/db.tables.json")) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write("[".getBytes());
                for (Format format : cntr.getFormatList(null).values()) {
                    if (baos.size() >= "[".getBytes().length) {
                        baos.write(",".getBytes());
                    }
                    FormatSerializer writer = new FormatSerializer(format);
                    writer.writeToJson(baos);
                }
                baos.write("]".getBytes());
                //String xmlContent = baos.toString("UTF-8");
                //System.out.println(xmlContent);
                fos.write(baos.toByteArray());
            }
        } finally {
            cntr.close();
        }
    }

    public void testOracleSQLBuilder() {
        // Test select SQL generating
        Format employeeFmt = new DataFormat("Testtable", Form.STRUCT);
        Format sqlParams = employeeFmt.addChild(SQLPARAMS, Form.STRUCT);
        sqlParams.addChild(CLAUSE, Form.FIELD, Type.STRING);
        employeeFmt.addChild(new DataFormat("Id", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("name", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("job_title", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("department", Form.FIELD, Type.STRING));
        OracleQuerySQLBuilder sql = new OracleQuerySQLBuilder(employeeFmt);
        Element employeeQuery = new DataElement(employeeFmt);
        employeeQuery.addChild(SQLPARAMS).addChild(CLAUSE).setValue("where Id = 'E10001' order by Id group by department");
        String sqlSelect = sql.generateSelectSQL(employeeQuery);
        System.out.println("Generated select SQL: " + sqlSelect);
        assertTrue("SELECT Id, name, job_title, department FROM Testtable where Id = 'E10001' order by Id group by department".equalsIgnoreCase(sqlSelect));

        // Test insert SQL generating
        Element employee = new DataElement(employeeFmt);
        employee.addChild(SQLPARAMS).addChild(CLAUSE).setValue("where Id = 'E10001'");
        employee.addChild("Id").setValue("E10001");
        employee.addChild("name").setValue("James");
        employee.addChild("job_title").setValue("Software engineer");
        employee.addChild("department").setValue("Software R&D");
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
        Format sqlParams = employeeFmt.addChild(SQLPARAMS, Form.STRUCT);
        sqlParams.addChild(CLAUSE, Form.FIELD, Type.STRING);
        sqlParams.addChild(ACTION, Form.FIELD, Type.STRING);
        employeeFmt.addChild(new DataFormat("EMPLOYEE_ID", Form.FIELD, Type.INTEGER));
        employeeFmt.addChild(new DataFormat("FIRST_NAME", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("LAST_NAME", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("EMAIL", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("PHONE_NUMBER", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("HIRE_DATE", Form.FIELD, Type.DATE));
        employeeFmt.addChild(new DataFormat("JOB_ID", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("SALARY", Form.FIELD, Type.FLOAT));
        employeeFmt.addChild(new DataFormat("COMMISSION_PCT", Form.FIELD, Type.INTEGER));
        employeeFmt.addChild(new DataFormat("MANAGER_ID", Form.FIELD, Type.INTEGER));
        employeeFmt.addChild(new DataFormat("DEPARTMENT_ID", Form.FIELD, Type.INTEGER));

        Format employeeFmtTest = new DataFormat("EMPLOYEES_TEST", Form.STRUCT);
        Format sqlParamsTest = employeeFmtTest.addChild(SQLPARAMS, Form.STRUCT);
        sqlParamsTest.addChild(CLAUSE, Form.FIELD, Type.STRING);
        sqlParamsTest.addChild(ACTION, Form.FIELD, Type.STRING);
        employeeFmtTest.addChild(new DataFormat("EMPLOYEE_ID", Form.FIELD, Type.INTEGER));
        employeeFmtTest.addChild(new DataFormat("FIRST_NAME", Form.FIELD, Type.STRING));
        employeeFmtTest.addChild(new DataFormat("LAST_NAME", Form.FIELD, Type.STRING));
        employeeFmtTest.addChild(new DataFormat("EMAIL", Form.FIELD, Type.STRING));
        employeeFmtTest.addChild(new DataFormat("PHONE_NUMBER", Form.FIELD, Type.STRING));
        employeeFmtTest.addChild(new DataFormat("HIRE_DATE", Form.FIELD, Type.DATE));
        employeeFmtTest.addChild(new DataFormat("JOB_ID", Form.FIELD, Type.STRING));
        employeeFmtTest.addChild(new DataFormat("SALARY", Form.FIELD, Type.FLOAT));
        employeeFmtTest.addChild(new DataFormat("COMMISSION_PCT", Form.FIELD, Type.INTEGER));
        employeeFmtTest.addChild(new DataFormat("MANAGER_ID", Form.FIELD, Type.INTEGER));
        employeeFmtTest.addChild(new DataFormat("DEPARTMENT_ID", Form.FIELD, Type.INTEGER));

        StringBuilder alterSession = new StringBuilder();
        alterSession.append("alter session set NLS_DATE_FORMAT='yyyy-mm-dd'");
        OracleClient client = new OracleClient("file:///C:/app/washaofe/product/11.2.0/dbhome/jdbc/lib/ojdbc6.jar",
                                               "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr", alterSession.toString());
        client.open();

        OracleOperation oo = new OracleOperation(client);
        Element select = new DataElement(employeeFmt);
        select.addChild(SQLPARAMS).addChild(ACTION).setValue("SELECT");
        OperationResult result = oo.execute(new ElementChunk(Arrays.asList(select)), employeeFmt);

        System.out.println("Got recoreds: " + result.getResult().size());
        TransformRule rule = new TransformRule(employeeFmtTest);
        rule.getRuleItem("EMPLOYEES_TEST.SQLParams.action").setScript("'INSERT'");
        rule.getRuleItem("EMPLOYEES_TEST.EMPLOYEE_ID").setScript("@EMPLOYEE.EMPLOYEE_ID");
        rule.getRuleItem("EMPLOYEES_TEST.FIRST_NAME").setScript("return @EMPLOYEE.FIRST_NAME + '-test'");
        rule.getRuleItem("EMPLOYEES_TEST.LAST_NAME").setScript("return (@EMPLOYEE.LAST_NAME + '-' + dateToString(now(), 'yyyy-MM-dd HH:mm:ss SSS').substr(11))");
        rule.getRuleItem("EMPLOYEES_TEST.EMAIL").setScript("@EMPLOYEE.EMAIL");
        rule.getRuleItem("EMPLOYEES_TEST.PHONE_NUMBER").setScript("@EMPLOYEE.PHONE_NUMBER");
        rule.getRuleItem("EMPLOYEES_TEST.HIRE_DATE").setScript("return dateToString(@EMPLOYEE.HIRE_DATE, 'yyyy-MM-dd')");
        rule.getRuleItem("EMPLOYEES_TEST.JOB_ID").setScript("@EMPLOYEE.JOB_ID");
        rule.getRuleItem("EMPLOYEES_TEST.SALARY").setScript("@EMPLOYEE.SALARY");
        rule.getRuleItem("EMPLOYEES_TEST.COMMISSION_PCT").setScript("@EMPLOYEE.COMMISSION_PCT");
        rule.getRuleItem("EMPLOYEES_TEST.MANAGER_ID").setScript("@EMPLOYEE.MANAGER_ID");
        rule.getRuleItem("EMPLOYEES_TEST.DEPARTMENT_ID").setScript("@EMPLOYEE.DEPARTMENT_ID");

        Processor transformMappter = new TransformMapper();
        List<Element> employeeTest = new ArrayList<>();
        List<Element> resultList = (List<Element>) transformMappter.execute(rule, result.getResult());
        employeeTest.addAll(resultList);
        assertTrue(employeeTest.size() == result.getResult().size());
        oo.execute(new ElementChunk(employeeTest), null);

        // Ending
        client.close();
    }

    public void testAddin() throws Exception {
        AddinEngine ae = new AddinEngine(ConnectorTest.class.getClassLoader());
        ae.start();
        AddinContext ac = ae.getAddinContext();
        Activator activator = new Activator();
        activator.start(ac);
        ServiceEntity<ConnectorFactory> se = ac.getService("type-oracle-jdbc");
        System.out.println(Arrays.toString(se.getPropertList().entrySet().toArray()));
    }
}
