package com.lumens.connector;

import com.lumens.connector.database.client.DBWriteSQLBuilder;
import com.lumens.connector.database.client.oracle.OracleClient;
import com.lumens.connector.database.client.oracle.OracleConnectorFactory;
import com.lumens.connector.database.client.oracle.OracleConstants;
import static com.lumens.connector.database.client.oracle.OracleConstants.SQLPARAMS;
import com.lumens.connector.database.client.oracle.OracleOperation;
import com.lumens.connector.database.client.oracle.OracleQuerySQLBuilder;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.Value;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.Processor;
import com.lumens.processor.ProcessorExecutionContext;
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
public class ConnectorTest implements OracleConstants {

    public ConnectorTest() {
    }

    @Test
    public void testEmpty() {
    }

    public void testOracleConnector() throws Exception {
        Connector cntr = new OracleConnectorFactory().createConnector();
        try {
            HashMap<String, Value> props = new HashMap<>();
            props.put(OJDBC, new Value("file:///X:\\lumens\\dist\\3rdparty\\oracle\\jdbc\\ojdbc6.jar"));
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

    // TODO: need to mock db ENV
    // TODO: add a pareparation step prior unit test to add table into db. Disable first 
    //@Test
    public void testOracleSQLBuilder() throws Exception {
        // Test select SQL generating
        Format employeeFmt = new DataFormat("Testtable", Form.STRUCT);
        Format sqlParams = employeeFmt.addChild(SQLPARAMS, Form.STRUCT);
        sqlParams.addChild(WHERE, Form.FIELD, Type.STRING);
        sqlParams.addChild(ORDERBY, Form.FIELD, Type.STRING);
        sqlParams.addChild(GROUPBY, Form.FIELD, Type.STRING);
        sqlParams.addChild(ACTION, Form.FIELD, Type.STRING);
        employeeFmt.addChild(new DataFormat("Id", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("name", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("job_title", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("department", Form.FIELD, Type.STRING));
        OracleQuerySQLBuilder sql = new OracleQuerySQLBuilder(employeeFmt);
        Element employeeQuery = new DataElement(employeeFmt);
        Element sqlEParams = employeeQuery.addChild(SQLPARAMS);
        sqlEParams.addChild(WHERE).setValue("Id = 'E10001'");
        sqlEParams.addChild(ORDERBY).setValue("Id");
        sqlEParams.addChild(GROUPBY).setValue("department");

        String sqlSelect = sql.generateSelectSQL(employeeQuery);
        System.out.println("Generated select SQL: " + sql.generatePageSQL(sqlSelect, 0, 100));
        System.out.println("Generated select SQL: " + sql.generatePageSQL(sqlSelect, 100, 200));
        //assertTrue("SELECT Id, name, job_title, department FROM Testtable where (Id = 'E10001') order by Id group by department".equalsIgnoreCase(String.format(sqlSelect, 0, 100)));

        // Test insert SQL generating
        Element employee = new DataElement(employeeFmt);
        employee.addChild(SQLPARAMS).addChild(WHERE).setValue("Id = 'E10001'");
        employee.addChild("Id").setValue("E10001");
        employee.addChild("name").setValue("James");
        employee.addChild("job_title").setValue("Software engineer");
        employee.addChild("department").setValue("Software R&D");
        employee.getChild(SQLPARAMS).addChild(ACTION).setValue("INSERT");
        DBWriteSQLBuilder sqlWrite = new DBWriteSQLBuilder();
        String sqlInsert = sqlWrite.generateInsertSQL(employee);
        System.out.println("Generated insert SQL: " + sqlInsert);

        // Test update SQL generating
        employee.getChild(SQLPARAMS).getChild(ACTION).setValue("UPDATE");
        employee.getChild(SQLPARAMS).getChild(WHERE).setValue("name LIKE 'J%'");
        String sqlUpdate = sqlWrite.generateUpdateSQL(employee);
        System.out.println("Generated update SQL: " + sqlUpdate);

        Format employeeFmtTest = new DataFormat("EMPLOYEES_TEST", Form.STRUCT);
        Format sqlParamsTest = employeeFmtTest.addChild(SQLPARAMS, Form.STRUCT);
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
        Element select = new DataElement(employeeFmtTest);
        select.addChild(SQLPARAMS).addChild(ACTION).setValue("SELECT");
        OracleQuerySQLBuilder sqlTest = new OracleQuerySQLBuilder(employeeFmtTest);
        sqlSelect = sqlTest.generateSelectSQL(select);
        System.out.println("Generated select SQL: " + sqlTest.generatePageSQL(sqlSelect, 1, 100));

        OracleClient client = new OracleClient(new MockOracleConnector("file:///X:\\lumens\\dist\\3rdparty\\oracle\\jdbc\\ojdbc6.jar",
                                                                       "jdbc:oracle:thin:@localhost:1521:orcl", "hr", "hr", "alter session set NLS_DATE_FORMAT='yyyy-mm-dd'", 50));
        client.open();
        client.getFormatList(Direction.IN, true);
        OracleOperation oo = new OracleOperation(client);
        OperationResult result = oo.execute(new ElementChunk(Arrays.asList(select)), employeeFmtTest);
        assertTrue(result.getData().size() == 50);
        //while (result.hasData()) {
        //    System.out.println("current query size: " + result.getData().size());
        //    result.executeNext();
        //}
        client.close();
    }

    public void testOracleOperation() throws Exception {
        Format employeeFmt = new DataFormat("EMPLOYEES", Form.STRUCT);
        Format sqlParams = employeeFmt.addChild(SQLPARAMS, Form.STRUCT);
        sqlParams.addChild(WHERE, Form.FIELD, Type.STRING);
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
        sqlParamsTest.addChild(WHERE, Form.FIELD, Type.STRING);
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
        OracleClient client = new OracleClient(new MockOracleConnector("file:///X:\\lumens\\dist\\3rdparty\\oracle\\jdbc\\ojdbc6.jar",
                                                                       "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr", alterSession.toString(), 100));
        client.open();

        OracleOperation oo = new OracleOperation(client);
        Element select = new DataElement(employeeFmt);
        select.addChild(SQLPARAMS).addChild(ACTION).setValue("SELECT");
        select.getChild(SQLPARAMS).addChild(WHERE).setValue("EMPLOYEE_ID = 100");
        OperationResult result = oo.execute(new ElementChunk(Arrays.asList(select)), employeeFmt);

        System.out.println("Got recoreds: " + result.getData().size());
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
        List<Element> resultList = (List<Element>) transformMappter.execute(new ProcessorExecutionContext(rule, result.getData()));
        employeeTest.addAll(resultList);
        assertTrue(employeeTest.size() == result.getData().size());
        //oo.execute(new ElementChunk(employeeTest), null);

        // Ending
        client.close();
    }

}
