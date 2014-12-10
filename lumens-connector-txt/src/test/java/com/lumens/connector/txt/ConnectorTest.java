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

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class ConnectorTest 
{
    private String path = getClass().getResource("/delimited/incsv.csv").getFile();
    private String schemaPath = getClass().getResource("/delimited/incsv_schema.xml").getFile();
    
    @Before
    public void testConnection() {
        try{
            File file = new File(path);
            if( file.isFile() && file.exists() ){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
                BufferedReader bufReader = new BufferedReader(reader);
                String lineTxt = null;
                while((lineTxt = bufReader.readLine()) != null ){
                    System.out.println(lineTxt);                    
                }
            }     
        }catch (Exception e) {
            System.out.println("Error on reading file content.");
            e.printStackTrace();
            assertFalse("Fail to connect", true);
        }
    }    
    
    @Test
    public void testConnector(){
        ConnectorFactory cntr = new TextConnectorFactory();
        TextConnector txt = (TextConnector)cntr.createConnector();
        
        Map<String, Value> props = new HashMap<>();
        props.put(TextConstants.ESCAPECHAR, new Value("\\"));
        props.put(TextConstants.FILEDELIMITER, new Value(","));
        props.put(TextConstants.SCHEMAPATH, new Value(schemaPath));
        props.put(TextConstants.MAXLINE, new Value(1000));
        props.put(TextConstants.ENCODING, new Value("UTF-8"));
        txt.setPropertyList(props);
        txt.open();
        
        // test get format list
        Map<String, Format> fmtList = txt.getFormatList(Direction.IN);
        if( fmtList.isEmpty()){
            assertFalse("Fail to get format list", true);
        }
        
        // test operation
        Operation oper = txt.getOperation();
        assertTrue("fail to open text connector", txt.isOpen());
        
        oper.begin();
        try{
            Format fmt = fmtList.get(TextConstants.FORMAT_FORMAT);            
            if( fmt == null ){
                assertFalse("Fail to get format", true);
            }
        
            fmt.addChild(TextConstants.OPERATION, Form.FIELD);            
            fmt.addChild(TextConstants.PATH, Form.FIELD);
            fmt.addChild(TextConstants.ENCODING, Form.FIELD);
            Element elem = new DataElement(fmt);
            elem.addChild(TextConstants.OPERATION).setValue(new Value(TextConstants.OPERATION_READ));            
            elem.addChild(TextConstants.PATH).setValue(new Value(path));
            elem.addChild(TextConstants.ENCODING).setValue(new Value("UTF-8"));
            List<Element> input = new ArrayList();
            input.add(elem);
            OperationResult result = oper.execute(input, fmt);            
            assertTrue("Fail to executre element", result.hasResult());
        }catch(Exception ex){
            assertFalse("Fail to execute element", true);
        }
                
        oper.commit();
        oper.end();
        txt.close();        
    }
    
    @Test
    public void testSchema(){
        File schema = new File(schemaPath);
        Map<String, Value> propList = new HashMap<>();
        propList.put(TextConstants.ENCODING, new Value("UTF-8"));
        propList.put(TextConstants.ESCAPECHAR, new Value("\\"));
        propList.put(TextConstants.FILEDELIMITER, new Value(","));
        propList.put(TextConstants.SCHEMAPATH, new Value(schemaPath));
        propList.put(TextConstants.MAXLINE, new Value(1000));
        propList.put(TextConstants.ENCODING, new Value("UTF-8"));
        if( schema.isFile() && schema.exists() ){
            TextFormatBuilder xsdReader = new TextFormatBuilder(propList);
            xsdReader.initalize();
            Map<String, Format> formats = xsdReader.getFormatList(Direction.IN);
        }       
    }
    
    @Test
    public void testClient(){    
        
    }
       
    @Test
    public void testElementBuilder(){
        
    }
}
