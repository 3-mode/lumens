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
    private String path2read = getClass().getResource("/delimited/incsv.csv").getFile();
    private String path2write = getClass().getResource("/delimited").getPath() + "/outcsv.csv";
    private String schemaPath = getClass().getResource("/delimited/incsv_schema.xml").getFile();
    
    @Before
    public void testConnection() {
        try{
            File file = new File(path2read);
            if( file.isFile() && file.exists() ){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(path2read), "UTF-8");
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
        props.put(TextConstants.LINEDELIMITER, new Value("\n")); 
        props.put(TextConstants.OPTION_FORMAT_ASTITLE, new Value(true));
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
        
            // Element read
            Element elemRead = new DataElement(fmt);
            Element paramsR = elemRead.addChild(TextConstants.FORMAT_PARAMS);            
            paramsR.addChild(TextConstants.OPERATION).setValue(new Value(TextConstants.OPERATION_READ));            
            paramsR.addChild(TextConstants.PATH).setValue(new Value(path2read));
            List<Element> input = new ArrayList();
            input.add(elemRead);
            OperationResult result = oper.execute(input, fmt);            
            assertTrue("Fail to executre element read", result.hasResult());
            
            // Element overwrite
            Element elemWrite = new DataElement(fmt);
            Element paramsW = elemWrite.addChild(TextConstants.FORMAT_PARAMS); 
            paramsW.addChild(TextConstants.OPERATION).setValue(new Value(TextConstants.OPERATION_OVERWRITE));                        
            paramsW.addChild(TextConstants.PATH).setValue(new Value(path2write));
            paramsW.addChild(TextConstants.ENCODING).setValue(new Value("UTF-8"));
            paramsW.addChild(TextConstants.FILEDELIMITER).setValue(new Value(","));
            paramsW.addChild(TextConstants.LINEDELIMITER).setValue(new Value("\n"));
            
            elemWrite.addChild("number").setValue(new Value("100"));
            elemWrite.addChild("text").setValue(new Value("text100"));
            elemWrite.addChild("date").setValue(new Value("2014-12-12"));
            elemWrite.addChild("available").setValue(new Value(true));            
            
            List<Element> output = new ArrayList();            
            output.add(elemWrite);
            //output.addAll(resultList);
            result = oper.execute(output, fmt);            
            assertTrue("Fail to executre element write", result.hasResult());
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
