/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.sqlserver;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.addin.ServiceEntity;
import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.OperationResult;
import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_CLAUSE;
import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_OPERATION;
import com.lumens.connector.database.client.sqlserver.SqlServerClient;
import com.lumens.connector.database.client.sqlserver.SqlServerConnectorFactory;
import com.lumens.connector.database.DatabaseConstants;
import static com.lumens.connector.database.DatabaseConstants.CONST_CNTR_SQLSERVER_FIELDS;
import com.lumens.connector.database.client.sqlserver.SqlServerOperation;
import com.lumens.connector.database.client.sqlserver.service.Activator;
import com.lumens.connector.database.client.sqlserver.sql.SqlServerQuerySQLBuilder;
import com.lumens.connector.database.client.sqlserver.sql.SqlServerWriteSQLBuilder;
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

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class ConnectorTest {
    private final String OJDBC_VAL = "file:///C:/lib/jdbc/sqljdbc4.jar";
    private final String CONNECTION_URL_Val = "jdbc:sqlserver://localhost\\hr:1433;databaseName=hr";
    private final String USER_Val = "hr";
    private final String PASSWORD_Val = "hr";
    private final String TABLE_NAM = "employee";

    public ConnectorTest() {
    }

    @Before
    public void testSqlServerConnection() {
        StringBuilder alterSession = new StringBuilder();
        SqlServerClient client = new SqlServerClient(OJDBC_VAL,
                                                     CONNECTION_URL_Val,
                                                     USER_Val, PASSWORD_Val,
                                                     alterSession.toString());
        try {
            client.open();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            client.close();
        }
    }

    @Test
    public void testSqlServerConnector() throws Exception {
        Connector cntr = new SqlServerConnectorFactory().createConnector();
        HashMap<String, Value> props = new HashMap();
        props.put(DatabaseConstants.OJDBC, new Value(OJDBC_VAL));
        props.put(DatabaseConstants.CONNECTION_URL, new Value(CONNECTION_URL_Val));
        props.put(DatabaseConstants.USER, new Value(USER_Val));
        props.put(DatabaseConstants.PASSWORD, new Value(PASSWORD_Val));
        props.put(DatabaseConstants.FULL_LOAD, new Value(true));
        props.put(DatabaseConstants.SESSION_ALTER, new Value(""));
        cntr.setPropertyList(props);

        try {
            cntr.open();
            FileOutputStream fos = new FileOutputStream("d:/sqlserver.tables.json");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write("[".getBytes());
            for (Format format : cntr.getFormatList(null).values()) {
                if (baos.size() >= "[".getBytes().length)
                    baos.write(",".getBytes());
                FormatSerializer writer = new FormatSerializer(format);
                writer.writeToJson(baos);
            }
            baos.write("]".getBytes());
            //String xmlContent = baos.toString("UTF-8");
            //System.out.println(xmlContent);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cntr.close();
        }
    }

    @Test
    public void testSqlServerSQL() {
        StringBuilder alterSession = new StringBuilder();
        SqlServerClient client = new SqlServerClient(OJDBC_VAL,
                                                     CONNECTION_URL_Val,
                                                     USER_Val, PASSWORD_Val,
                                                     alterSession.toString());
        try {
            client.open();
            client.execute("select * from employee");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            client.close();
        }
    }

    @Test
    public void testSqlServerSQLBuilder() {
        // Test select SQL generating
        Format employeeFmt = new DataFormat("Testtable", Form.STRUCT);
        employeeFmt.addChild(CONST_CNTR_SQLSERVER_CLAUSE, Form.FIELD);
        Format fields = employeeFmt.addChild(CONST_CNTR_SQLSERVER_FIELDS, Form.STRUCT);
        fields.addChild(new DataFormat("Id", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("name", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("job_title", Form.FIELD, Type.STRING));
        fields.addChild(new DataFormat("department", Form.FIELD, Type.STRING));
        SqlServerQuerySQLBuilder sql = new SqlServerQuerySQLBuilder(employeeFmt);
        Element employeeQuery = new DataElement(employeeFmt);
        employeeQuery.addChild(CONST_CNTR_SQLSERVER_CLAUSE).setValue("where Id = 'E10001' order by Id group by department");
        String sqlSelect = sql.generateSelectSQL(employeeQuery);
        System.out.println("Generated select SQL: " + sqlSelect);
        assertTrue("SELECT Id, name, job_title, department FROM Testtable where Id = 'E10001' order by Id group by department".equalsIgnoreCase(sqlSelect));

        // Test insert SQL generating
        Element employee = new DataElement(employeeFmt);
        employee.addChild(CONST_CNTR_SQLSERVER_CLAUSE).setValue("where Id = 'E10001'");
        Element dfields = employee.addChild(CONST_CNTR_SQLSERVER_FIELDS);
        dfields.addChild("Id").setValue("E10001");
        dfields.addChild("name").setValue("James");
        dfields.addChild("job_title").setValue("Software engineer");
        dfields.addChild("department").setValue("Software R&D");
        SqlServerWriteSQLBuilder sqlWrite = new SqlServerWriteSQLBuilder();
        String sqlInsert = sqlWrite.generateInsertSQL(employee);
        System.out.println("Generated insert SQL: " + sqlInsert);

        // Test update SQL generating
        employee.setValue("where name like 'J%'");
        String sqlUpdate = sqlWrite.generateUpdateSQL(employee);
        System.out.println("Generated update SQL: " + sqlUpdate);
    }

    @Test
    public void testSqlServerOperation() throws Exception {
        Format employeeFmt = new DataFormat("employees", Form.STRUCT);
        employeeFmt.addChild(CONST_CNTR_SQLSERVER_CLAUSE, Form.FIELD);
        employeeFmt.addChild(CONST_CNTR_SQLSERVER_OPERATION, Form.FIELD);
        Format fields = employeeFmt.addChild(CONST_CNTR_SQLSERVER_FIELDS, Form.STRUCT);
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
        employeeFmtTest.addChild(CONST_CNTR_SQLSERVER_CLAUSE, Form.FIELD, Type.STRING);
        employeeFmtTest.addChild(CONST_CNTR_SQLSERVER_OPERATION, Form.FIELD, Type.STRING);
        Format fields_test = employeeFmtTest.addChild(CONST_CNTR_SQLSERVER_FIELDS, Form.STRUCT);
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
        SqlServerClient client = new SqlServerClient(OJDBC_VAL,
                                                     CONNECTION_URL_Val,
                                                     USER_Val, PASSWORD_Val,
                                                     alterSession.toString());
        client.open();

        SqlServerOperation oo = new SqlServerOperation(client);
        oo.begin();
        Element select = new DataElement(employeeFmt);
        select.addChild(CONST_CNTR_SQLSERVER_OPERATION).setValue("select");
        OperationResult result = oo.execute(Arrays.asList(select), employeeFmt);
        oo.end();

        System.out.println("Got recoreds: " + result.getResult().size());
        TransformRule rule = new TransformRule(employeeFmtTest);
        rule.getRuleItem("EMPLOYEES_TEST.operation").setScript("'INSERT'");
        rule.getRuleItem("EMPLOYEES_TEST.fields.EMPLOYEE_ID").setScript("@fields.EMPLOYEE_ID");
        rule.getRuleItem("EMPLOYEES_TEST.fields.FIRST_NAME").setScript("return @fields.FIRST_NAME + '-test'");
        rule.getRuleItem("EMPLOYEES_TEST.fields.LAST_NAME").setScript("return (@fields.LAST_NAME + '-' + dateToString(now(), 'yyyy-MM-dd HH:mm:ss SSS').substr(11))");
        rule.getRuleItem("EMPLOYEES_TEST.fields.EMAIL").setScript("@fields.EMAIL");
        rule.getRuleItem("EMPLOYEES_TEST.fields.PHONE_NUMBER").setScript("@fields.PHONE_NUMBER");
        rule.getRuleItem("EMPLOYEES_TEST.fields.HIRE_DATE").setScript("return dateToString(@fields.HIRE_DATE, 'yyyy-MM-dd')");
        rule.getRuleItem("EMPLOYEES_TEST.fields.JOB_ID").setScript("@fields.JOB_ID");
        rule.getRuleItem("EMPLOYEES_TEST.fields.SALARY").setScript("@fields.SALARY");
        rule.getRuleItem("EMPLOYEES_TEST.fields.COMMISSION_PCT").setScript("@fields.COMMISSION_PCT");
        rule.getRuleItem("EMPLOYEES_TEST.fields.MANAGER_ID").setScript("@fields.MANAGER_ID");
        rule.getRuleItem("EMPLOYEES_TEST.fields.DEPARTMENT_ID").setScript("@fields.DEPARTMENT_ID");

        Processor transformProcessor = new TransformMapper();
        List<Element> employeeTest = new ArrayList<>();
        List<Element> resultList = (List<Element>) transformProcessor.execute(rule, result.getResult());
        employeeTest.addAll(resultList);
        assertTrue(employeeTest.size() == result.getResult().size());
        oo.execute(employeeTest, null);

        // Ending
        client.close();
    }

    @Test
    public void testAddin() throws Exception {
        AddinEngine ae = new AddinEngine(ConnectorTest.class.getClassLoader());
        ae.start();
        AddinContext ac = ae.getAddinContext();
        Activator activator = new Activator();
        activator.start(ac);
        ServiceEntity<ConnectorFactory> se = ac.getService("type-sqlserver-jdbc");
        System.out.println(Arrays.toString(se.getPropertList().entrySet().toArray()));
    }
}
