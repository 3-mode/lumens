/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.connector.ElementChunk;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class ConnectorTest implements TextConstants {

    private String path2read = null;
    private String folder2read = null;
    private String path2write = null;
    private String schemaPath = null;
    private String folderSchemaPath = null;

    @Before
    public void testConnection() {
        try {
            path2read = getClass().getResource("/delimited/csv/rfc4180_LastLineBreakCN.txt").toURI().getPath();
            folder2read = getClass().getResource("/delimited/csv/").toURI().getPath();
            path2write = getClass().getResource("/delimited").toURI().getPath() + "/outcsv.txt";
            schemaPath = getClass().getResource("/delimited/incsv_schema.xml").toURI().getPath();
            folderSchemaPath = getClass().getResource("/delimited/csv/text_schema.xml").toURI().getPath();

            File file = new File(path2read);
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(path2read), "UTF-8");
                BufferedReader bufReader = new BufferedReader(reader);
                String lineTxt = null;                
                while ((lineTxt = bufReader.readLine()) != null) {                    
                }
            }
        } catch (Exception e) {
            System.out.println("Error on reading file content.");
            e.printStackTrace();
            assertFalse("Fail to connect", true);
        }
    }

    @Test
    public void testRFC4180() {
        CSVPattern parser = new RFC4180Parser();
        String escape = parser.GetEscapePattern();
        String nonescape = parser.GetNonEscapePattern();

        // "(?:[\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E]|,|\r|\n|"")+"|(?:[\x20-\x21]|[\x23-\x2B]|[\x2D-\x7E])+
        String field = String.format("%s|%s", escape, nonescape);
        String record = String.format("(?:%s,%s)*", field, field);

        String non_escape_string = "-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3";

        // abc, cde,"abc,","abc","abc,""","ab""""c,""",-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3                           
        String field_string0 = "abc, cde,\"abc,\",\"abc\",\"abc,\"\"\",\"ab\"\"\"\"c,\"\"\",-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3";
        // comma in head: ,abc, cde,"abc,","abc","abc,""","ab""""c,""",-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3 
        String field_string1 = ",abc, cde,\"abc,\",\"abc\",\"abc,\"\"\",\"ab\"\"\"\"c,\"\"\",-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3";
        // comma at last: abc, cde,"abc,","abc","abc,""","ab""""c,""",-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3,
        String field_string2 = "abc, cde,\"abc,\",\"abc\",\"abc,\"\"\",\"ab\"\"\"\"c,\"\"\",-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3.";
        // comma and space in head:  ,abc, cde,"abc,","abc","abc,""","ab""""c,""",-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3 
        String field_string3 = " , cde,\"abc,\",\"abc\",\"abc,\"\"\",\"ab\"\"\"\"c,\"\"\",-   !#$%&abCDE*1,   !#$%&abCDE*2,---   !#$%&abCDE*3 ";

        String[] fields = {field_string0, field_string1, field_string2, field_string3};

        // Field test
        for (String field_string : fields) {
            Pattern p = Pattern.compile(field);
            Matcher m = p.matcher(field_string);
            String sub = null;
            List<String> list = new ArrayList();
            while (m.find()) {
                sub = m.group();
                list.add(sub);
            }
            assertTrue("field test fail: size not be equal", list.size() == 9);
        }

        String escape_string1 = "\"abc,\"";             // Normal: "abc,"        
        String escape_string2 = "\"abc,\"\"\"\"";      // Missing last ":"abc,""""        
        String escape_string3 = "\"abc,\"\"";           // Missing first ":"abc,""        
        String escape_string4 = "\"abc,\"\"\\r\\n\"";  // with CRLF: "abc,""\r\n"        

        // Escape test         
        assertTrue("escape test fail", escape_string1.matches(escape));
        assertFalse("escape test fail", escape_string2.matches(escape));
        assertFalse("escape test fail", escape_string3.matches(escape));
        assertTrue("escape test fail", escape_string4.matches(escape));
        // Non escape test
        assertFalse("non escape test fail", non_escape_string.matches(escape));

    }

    @Test
    public void testConnectorReadFolder() {
        ConnectorFactory cntr = new TextConnectorFactory();
        TextConnector cntrR = (TextConnector) cntr.createConnector();

        Map<String, Value> propsR = new HashMap<>();
        propsR.put(ESCAPE_CHAR, new Value("\""));
        propsR.put(QUOTE_CHAR, new Value("\""));
        propsR.put(FILEDELIMITER, new Value(","));
        propsR.put(SCHEMA_PATH, new Value(folderSchemaPath));
        propsR.put(OPTION_MAXLINE, new Value(1000));
        propsR.put(ENCODING, new Value("utf-8"));
        propsR.put(LINEDELIMITER, new Value("\n"));
        propsR.put(OPTION_IGNORE_EMPTYLINE, new Value(true));
        propsR.put(OPTION_MAXLINE, new Value(9));
        propsR.put(FILE_EXTENSION, new Value("txt"));
        propsR.put(FILE_FILTER, new Value("*.txt"));
        propsR.put(OPTION_FORMAT_ASTITLE, new Value(true));
        propsR.put(OPTION_FIRST_LINE_ASTITLE, new Value(false));
        propsR.put(OPTION_IGNORE_READLINE_ERROR, new Value(true));
        propsR.put(OPTION_TRIM_SPACE, new Value(false));
        propsR.put(OPTION_SKIP_COMMENTS, new Value(false));
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
        Format fmtR = fmtListR.get("TextMessage");
        if (fmtR == null) {
            assertFalse("Fail to get source format", true);
        }

        try {
            // Read a folder
            Element elemMultiRead = new DataElement(fmtR);
            Element paramsMultiR = elemMultiRead.addChild(FORMAT_PARAMS);
            paramsMultiR.setValue(new Value(FORMAT_MESSAGE));
            paramsMultiR.addChild(OPERATION).setValue(new Value(OPERATION_READ));
            paramsMultiR.addChild(PATH).setValue(new Value(folder2read));
            resultR = operR.execute(new ElementChunk(Arrays.asList(elemMultiRead)), fmtR);
            assertTrue("Fail to executre source element read: multi files read folder", resultR.hasResult());

            System.out.println("-----------------------------------------");
            System.out.println("Reading multi csv files:");
            for (Element elem : resultR.getResult()) {
                StringBuilder line = new StringBuilder();
                for (Element el : elem.getChildren()) {
                    if (line.length() > 0) {
                        line.append(",");
                    }
                    line.append(el.getValue().toString());
                }
                System.out.println(line);
            }
        } catch (Exception ex) {
            assertFalse("Fail to execute source connector read: multi files read folder.\n " + ex.getMessage(), true);
        }

        cntrR.close();
    }

    @Test
    public void testConnectorReadCsv() {
        ConnectorFactory cntr = new TextConnectorFactory();
        TextConnector cntrR = (TextConnector) cntr.createConnector();

        Map<String, Value> propsR = new HashMap<>();
        propsR.put(ESCAPE_CHAR, new Value("\""));
        propsR.put(QUOTE_CHAR, new Value("\""));
        propsR.put(FILEDELIMITER, new Value(","));
        propsR.put(SCHEMA_PATH, new Value(schemaPath));
        propsR.put(OPTION_MAXLINE, new Value(1000));
        propsR.put(ENCODING, new Value("UTF-8"));
        propsR.put(LINEDELIMITER, new Value("\n"));
        propsR.put(OPTION_IGNORE_EMPTYLINE, new Value(true));
        propsR.put(OPTION_MAXLINE, new Value(9));
        propsR.put(FILE_EXTENSION, new Value("csv"));
        propsR.put(FILE_FILTER, new Value("*.csv"));
        propsR.put(OPTION_FORMAT_ASTITLE, new Value(true));
        propsR.put(OPTION_FIRST_LINE_ASTITLE, new Value(false));
        propsR.put(OPTION_IGNORE_READLINE_ERROR, new Value(true));
        propsR.put(OPTION_TRIM_SPACE, new Value(true));
        propsR.put(OPTION_SKIP_COMMENTS, new Value(false));
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

        // Read
        try {
            Format fmtR = fmtListR.get("TextMessage");
            if (fmtR == null) {
                assertFalse("Fail to get source format", true);
            }

            // Element read            
            Element elemRead = new DataElement(fmtR);
            Element paramsR = elemRead.addChild(FORMAT_PARAMS);
            paramsR.setValue(new Value(FORMAT_MESSAGE));
            paramsR.addChild(OPERATION).setValue(new Value(OPERATION_READ));
            paramsR.addChild(PATH).setValue(new Value(path2read));

            resultR = operR.execute(new ElementChunk(Arrays.asList(elemRead)), fmtR);
            assertTrue("Fail to executre source element read", resultR.hasResult());
            System.out.println("-----------------------------------------");
            System.out.println("Reading single csv file:");
            for (Element elem : resultR.getResult()) {
                StringBuilder line = new StringBuilder();
                for (Element el : elem.getChildren()) {
                    if (line.length() > 0) {
                        line.append(",");
                    }
                    line.append(el.getValue().toString());
                }
                System.out.println(line);
            }
            cntrR.close();
        } catch (Exception ex) {
            assertFalse("Fail to execute source connector read: single file read.\n " + ex.getMessage(), true);
        }
    }

    @Test
    public void testConnectorWrite() {
        ConnectorFactory cntr = new TextConnectorFactory();
        // Element overwrite
        TextConnector cntrW = (TextConnector) cntr.createConnector();

        Map<String, Value> propsW = new HashMap<>();
        propsW.put(ESCAPE_CHAR, new Value("\\"));
        propsW.put(FILEDELIMITER, new Value("|"));
        propsW.put(SCHEMA_PATH, new Value(schemaPath));
        propsW.put(PATH, new Value(path2write));
        propsW.put(ENCODING, new Value("UTF-8"));
        propsW.put(LINEDELIMITER, new Value("\r\n"));
        propsW.put(OPTION_FORMAT_ASTITLE, new Value(true));
        propsW.put(OPTION_TRIM_SPACE, new Value(true));
        propsW.put(OPTION_SKIP_COMMENTS, new Value(false));
        propsW.put(OPTION_QUOTE_MODE, new Value(true));
        propsW.put(QUOTE_CHAR, new Value("\""));
        cntrW.setPropertyList(propsW);
        cntrW.open();

        // test get format list
        Map<String, Format> fmtListW = cntrW.getFormatList(Direction.OUT);
        if (fmtListW.isEmpty()) {
            assertFalse("Fail to get destination format list", true);
        }

        // Write
        Operation operW = cntrW.getOperation();
        assertTrue("fail to open destination text connector", cntrW.isOpen());
        try {
            Format fmtW = fmtListW.get("TextMessage");
            if (fmtW == null) {
                assertFalse("Fail to get destination format", true);
            }
            Element elemWrite = new DataElement(fmtW);
            Element paramsW = elemWrite.addChild(FORMAT_PARAMS);
            paramsW.setValue(new Value(FORMAT_MESSAGE));
            paramsW.addChild(OPERATION).setValue(new Value(OPERATION_OVERWRITE));
            paramsW.addChild(PATH).setValue(new Value(path2write));
            paramsW.addChild(ENCODING).setValue(new Value("UTF-8"));
            paramsW.addChild(FILEDELIMITER).setValue(new Value("|"));
            paramsW.addChild(LINEDELIMITER).setValue(new Value("\r\n"));

            elemWrite.addChild("number").setValue(new Value("100"));
            elemWrite.addChild("text").setValue(new Value("测试"));
            elemWrite.addChild("date").setValue(new Value("2014-12-12"));
            elemWrite.addChild("available").setValue(new Value(true));

            List<Element> output = new ArrayList();
            output.add(elemWrite);
            System.out.println("-----------------------------------------");
            System.out.println("Content to write:");
            StringBuilder line = new StringBuilder();
            for (Element el : elemWrite.getChildren()) {
                if (line.length() > 0) {
                    line.append(",");
                }
                line.append(el.getValue().toString());
            }
            System.out.println(line);
            System.out.println();

            OperationResult result = operW.execute(new ElementChunk(output), fmtW);
        } catch (Exception ex) {
            assertFalse("Fail to execute source connector write.\n " + ex.getMessage(), true);
        }

        // Append
        try {
            Format fmtA = fmtListW.get("TextMessage");
            if (fmtA == null) {
                assertFalse("Fail to get destination format", true);
            }
            Element elemAppend = new DataElement(fmtA);
            Element paramsA = elemAppend.addChild(FORMAT_PARAMS);
            paramsA.setValue(new Value(FORMAT_MESSAGE));
            paramsA.addChild(OPERATION).setValue(new Value(OPERATION_APPEND));
            paramsA.addChild(PATH).setValue(new Value(path2write));
            paramsA.addChild(ENCODING).setValue(new Value("UTF-8"));
            paramsA.addChild(FILEDELIMITER).setValue(new Value("***"));
            paramsA.addChild(LINEDELIMITER).setValue(new Value("\r\n"));

            elemAppend.addChild("number").setValue(new Value("99"));
            elemAppend.addChild("text").setValue(new Value("append"));
            elemAppend.addChild("date").setValue(new Value("2014-12-25"));
            elemAppend.addChild("available").setValue(new Value(false));

            List<Element> outputA = new ArrayList();
            outputA.add(elemAppend);
            System.out.println("-----------------------------------------");
            System.out.println("Content to append:");
            StringBuilder lineA = new StringBuilder();
            for (Element elem : outputA) {
                for (Element el : elem.getChildren()) {
                    if (lineA.length() > 0) {
                        lineA.append(",");
                    }
                    lineA.append(el.getValue().toString());
                }
                System.out.println(lineA);
            }
            OperationResult resultA = operW.execute(new ElementChunk(outputA), fmtA);
        } catch (Exception ex) {
            assertFalse("Fail to execute source connector append.\n " + ex.getMessage(), true);
        }

        cntrW.close();
    }

    @Test
    public void testSchema() {
        File schema = new File(schemaPath);
        Map<String, Value> propList = new HashMap<>();
        propList.put(ENCODING, new Value("UTF-8"));
        propList.put(ESCAPE_CHAR, new Value("\\"));
        propList.put(FILEDELIMITER, new Value(","));
        propList.put(SCHEMA_PATH, new Value(schemaPath));
        propList.put(OPTION_MAXLINE, new Value(1000));
        propList.put(ENCODING, new Value("UTF-8"));
        if (schema.isFile() && schema.exists()) {
            TextFormatBuilder xsdReader = new TextFormatBuilder(propList);
            xsdReader.initalize();
            Map<String, Format> formats = xsdReader.getFormatList(Direction.IN);
        }
    }
}
