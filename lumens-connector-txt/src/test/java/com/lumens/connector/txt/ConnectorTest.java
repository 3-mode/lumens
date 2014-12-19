/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Value;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class ConnectorTest {

    private String path2read = getClass().getResource("/delimited/incsv.csv").getFile();
    private String path2write = getClass().getResource("/delimited").getPath() + "/outcsv.csv";
    private String schemaPath = getClass().getResource("/delimited/incsv_schema.xml").getFile();

    @Before
    public void testConnection() {
        try {
            File file = new File(path2read);
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(path2read), "UTF-8");
                BufferedReader bufReader = new BufferedReader(reader);
                String lineTxt = null;
                while ((lineTxt = bufReader.readLine()) != null) {
                    System.out.println(lineTxt);
                }
            }
        } catch (Exception e) {
            System.out.println("Error on reading file content.");
            e.printStackTrace();
            assertFalse("Fail to connect", true);
        }
    }

    @Test
    public void testConnector() {
        ConnectorFactory cntr = new TextConnectorFactory();
        TextConnector cntrR = (TextConnector) cntr.createConnector();

        Map<String, Value> propsR = new HashMap<>();
        propsR.put(TextConstants.ESCAPECHAR, new Value("\\"));
        propsR.put(TextConstants.FILEDELIMITER, new Value(","));
        propsR.put(TextConstants.SCHEMA_PATH, new Value(schemaPath));
        propsR.put(TextConstants.MAXLINE, new Value(1000));
        propsR.put(TextConstants.ENCODING, new Value("UTF-8"));
        propsR.put(TextConstants.LINEDELIMITER, new Value("\n"));
        propsR.put(TextConstants.OPTION_FORMAT_ASTITLE, new Value(true));
        cntrR.setPropertyList(propsR);
        cntrR.open();

        // test get format list
        Map<String, Format> fmtListR = cntrR.getFormatList(Direction.IN);
        if (fmtListR.isEmpty()) {
            assertFalse("Fail to get source format list", true);
        }

        // test operation
        Operation operR = cntrR.getOperation();
        assertTrue("fail to open source text connector", cntrR.isOpen());

        OperationResult resultR = null;
        operR.begin();
        try {
            Format fmtR = fmtListR.get("TextMessage");
            if (fmtR == null) {
                assertFalse("Fail to get source format", true);
            }

            // Element read
            Element elemRead = new DataElement(fmtR);
            Element paramsR = elemRead.addChild(TextConstants.FORMAT_PARAMS);
            paramsR.setValue(new Value(TextConstants.FORMAT_MESSAGE));
            paramsR.addChild(TextConstants.OPERATION).setValue(new Value(TextConstants.OPERATION_READ));
            paramsR.addChild(TextConstants.PATH).setValue(new Value(path2read));

            resultR = operR.execute(Arrays.asList(elemRead), fmtR);
            assertTrue("Fail to executre source element read", resultR.hasResult());
            operR.commit();
            operR.end();
        } catch (Exception ex) {
            assertFalse("Fail to execute source connector read", true);
        }
        
        // Element overwrite
        TextConnector cntrW = (TextConnector) cntr.createConnector();

        Map<String, Value> propsW = new HashMap<>();
        propsW.put(TextConstants.ESCAPECHAR, new Value("\\"));
        propsW.put(TextConstants.FILEDELIMITER, new Value("|"));
        propsW.put(TextConstants.SCHEMA_PATH, new Value(schemaPath));
        propsW.put(TextConstants.PATH, new Value(path2write));
        propsW.put(TextConstants.MAXLINE, new Value(1000));
        propsW.put(TextConstants.ENCODING, new Value("UTF-8"));
        propsW.put(TextConstants.LINEDELIMITER, new Value("\r\n"));
        propsW.put(TextConstants.OPTION_FORMAT_ASTITLE, new Value(true));
        cntrW.setPropertyList(propsW);
        cntrW.open();

        // test get format list
        Map<String, Format> fmtListW = cntrW.getFormatList(Direction.OUT);
        if (fmtListR.isEmpty()) {
            assertFalse("Fail to get destination format list", true);
        }

        // test operation
        Operation operW = cntrW.getOperation();
        assertTrue("fail to open destination text connector", cntrR.isOpen());
        try {
            Format fmtW = fmtListR.get("TextMessage");
            if (fmtW == null) {
                assertFalse("Fail to get source format", true);
            }            
            operR.begin();
            Element elemWrite = new DataElement(fmtW);
            Element paramsW = elemWrite.addChild(TextConstants.FORMAT_PARAMS);
            paramsW.setValue(new Value(TextConstants.FORMAT_MESSAGE));
            paramsW.addChild(TextConstants.OPERATION).setValue(new Value(TextConstants.OPERATION_OVERWRITE));
            paramsW.addChild(TextConstants.PATH).setValue(new Value(path2write));
            paramsW.addChild(TextConstants.ENCODING).setValue(new Value("UTF-8"));
            paramsW.addChild(TextConstants.FILEDELIMITER).setValue(new Value("|"));
            paramsW.addChild(TextConstants.LINEDELIMITER).setValue(new Value("\r\n"));

            elemWrite.addChild("number").setValue(new Value("100"));
            elemWrite.addChild("text").setValue(new Value("text100"));
            elemWrite.addChild("date").setValue(new Value("2014-12-12"));
            elemWrite.addChild("available").setValue(new Value(true));

            List<Element> output = new ArrayList();
            output.add(elemWrite);
            OperationResult result = operR.execute(output, fmtW);            
        } catch (Exception ex) {
            assertFalse("Fail to execute element", true);
        }

        cntrR.close();
    }

    @Test
    public void testSchema() {
        File schema = new File(schemaPath);
        Map<String, Value> propList = new HashMap<>();
        propList.put(TextConstants.ENCODING, new Value("UTF-8"));
        propList.put(TextConstants.ESCAPECHAR, new Value("\\"));
        propList.put(TextConstants.FILEDELIMITER, new Value(","));
        propList.put(TextConstants.SCHEMA_PATH, new Value(schemaPath));
        propList.put(TextConstants.MAXLINE, new Value(1000));
        propList.put(TextConstants.ENCODING, new Value("UTF-8"));
        if (schema.isFile() && schema.exists()) {
            TextFormatBuilder xsdReader = new TextFormatBuilder(propList);
            xsdReader.initalize();
            Map<String, Format> formats = xsdReader.getFormatList(Direction.IN);
        }
    }

    @Test
    public void testClient() {

    }

    @Test
    public void testElementBuilder() {

    }
}
