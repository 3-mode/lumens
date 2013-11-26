package com.lumens.connector;

import com.lumens.connector.database.DatabaseConnector;
import com.lumens.connector.database.DatabaseConstants;
import com.lumens.connector.database.client.oracle.OracleQuerySQLBuilder;
import com.lumens.connector.database.client.oracle.OracleWriteSQLBuilder;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.Value;
import com.lumens.model.serializer.FormatXmlSerializer;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ConnectorTest extends TestCase implements DatabaseConstants {
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
            props.put(OJDBC, new Value("file:///C:/app/washaofe/product/11.2.0/dbhome/jdbc/lib/ojdbc6.jar"));
            props.put(CONNECTION_URL, new Value("jdbc:oracle:thin:@localhost:1521:orcl"));
            props.put(USER, new Value("hr"));
            props.put(PASSWORD, new Value("hr"));
            props.put(FULL_LOAD, new Value(true));
            props.put(LETTER_UPPER, new Value(false));
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
        } finally {
            cntr.close();
        }
    }

    public void testOracleQuerySQLBuilder() {
        // Test select SQL generating
        Format employeeFmt = new DataFormat("Testtable", Form.STRUCT, Type.STRING);
        employeeFmt.addChild(new DataFormat("Id", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("name", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("job_title", Form.FIELD, Type.STRING));
        employeeFmt.addChild(new DataFormat("department", Form.FIELD, Type.STRING));
        OracleQuerySQLBuilder sql = new OracleQuerySQLBuilder(employeeFmt);
        Element employeeQuery = new DataElement(employeeFmt);
        employeeQuery.setValue("where Id = 'E10001' order by Id group by department");
        String sqlSelect = sql.generateSelectSQL(employeeQuery);
        System.out.println("Generated select SQL: " + sqlSelect);
        assertTrue("SELECT Id, name, job_title, department FROM Testtable where Id = 'E10001' order by Id group by department".equalsIgnoreCase(sqlSelect));

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
        employee.setValue("where name like 'J%'");
        String sqlUpdate = sqlWrite.generateUpdateSQL(employee);
        System.out.println("Generated update SQL: " + sqlUpdate);
    }
}
