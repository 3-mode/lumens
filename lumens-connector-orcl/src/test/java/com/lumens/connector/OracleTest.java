package com.lumens.connector;

/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.addin.ServiceEntity;
import com.lumens.connector.database.client.oracle.OracleClient;
import com.lumens.connector.database.client.oracle.service.Activator;
import com.lumens.model.Format;
import java.util.Arrays;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class OracleTest {

    public OracleTest() {
    }

    @Test
    public void testAddin() throws Exception {
        AddinEngine ae = new AddinEngine("en_US", OracleTest.class.getClassLoader());
        ae.start();
        AddinContext ac = ae.getAddinContext();
        Activator activator = new Activator();
        activator.start(ac);
        ServiceEntity<ConnectorFactory> se = ac.getService("type-oracle-jdbc");
        System.out.println(Arrays.toString(se.getPropertList().entrySet().toArray()));
    }

    // TODO need to mock DB ENV
    public void testFormatList() throws Exception {
        StringBuilder alterSession = new StringBuilder();
        alterSession.append("alter session set NLS_DATE_FORMAT='yyyy-mm-dd'");
        OracleClient client = new OracleClient(new MockOracleConnector("file:///X:\\lumens\\dist\\3rdparty\\oracle\\jdbc\\ojdbc6.jar",
                                                                       "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr", alterSession.toString(), 100));
        client.open();
        Map<String, Format> tables = client.getFormatList(Direction.IN, true);
        System.out.println("Loaded tables: " + tables.size());
        client.close();
    }
}
