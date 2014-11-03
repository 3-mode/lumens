/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.sqlserver;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.lumens.addin.AddinContext;
import com.lumens.addin.AddinEngine;
import com.lumens.addin.ServiceEntity;
import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.OperationResult;
import com.lumens.connector.database.DatabaseConstants;
import static com.lumens.connector.database.DatabaseConstants.CONNECTION_URL;
import static com.lumens.connector.database.DatabaseConstants.FULL_LOAD;
import static com.lumens.connector.database.DatabaseConstants.OJDBC;
import static com.lumens.connector.database.DatabaseConstants.PASSWORD;
import static com.lumens.connector.database.DatabaseConstants.USER;
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
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author whiskey
 */
public class ConnectorTest {
    
    public ConnectorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testSqlServerConnector() throws Exception {
    
    }

    @Test
    public void testSqlServerSQLBuilder() {
        
    }

    @Test
    public void testOracleOperation() throws Exception {
       
    }

    @Test
    public void testAddin() throws Exception {
        AddinEngine ae = new AddinEngine(ConnectorTest.class.getClassLoader());
        ae.start();
        AddinContext ac = ae.getAddinContext();
        Activator activator = new Activator();
        activator.start(ac);
        ServiceEntity<ConnectorFactory> se = ac.getService("type-sqlserver");
        System.out.println(Arrays.toString(se.getPropertList().entrySet().toArray()));
    }    
}
