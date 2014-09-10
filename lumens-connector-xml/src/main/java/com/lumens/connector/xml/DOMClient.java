/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.xml;

/**
 *  
 * @author whiskey
 * 
 * <?xml version="1.0" encoding="UTF-8"?>  
     <employees>  
       <!--An XML Note -->  
       <?target text?>  
       <employee id="473774" name="Wisper Guo">  
         <sex>M</sex>  
         <age>35</age>  
       </employee>  
       <employee id="463775" name="James Wang">  
         <sex>M</sex>  
         <age>36</age>  
       </employee>  
     </employees>  
*/

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import java.io.InputStream;
import java.io.OutputStream;
import org.xml.sax.SAXException;  
import java.io.IOException; 

public class DOMClient extends xmlClient {
    private Document xmlDoc;
    private String xmlPath;
    private DocumentBuilder builder = null;
    
    DOMClient(){
        super(null);
        init();
    }
    
    @Override
    public void init() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        }catch (ParserConfigurationException ex ) {
            ex.printStackTrace();;
        }
        
        xmlDoc = builder.newDocument();
    }
    
    @Override
    public void parseXml(InputStream in)
    {
        try{
            xmlDoc = builder.parse(in);        
        } catch (SAXException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }          
    }
    
    
    /*
      <root>
         <element attrName="attrVal"> val </element>
      </root>
    */
    public Element createElement(String name, String val, String attrName, String attrVal) {
        Element elem = xmlDoc.createElement(name);
        
        if (null != val){
            elem.appendChild(xmlDoc.createTextNode(val));
        }
        if ( null != attrName && null != val){
            Attr attr = createAttr(attrName, attrVal);
            elem.setAttributeNode(attr);
        }
        
        return elem;        
    }
   
    /*
       <element name=val></element>
    */
    public Attr createAttr(String name, String val){
        Attr attr = xmlDoc.createAttribute(name);
        attr.setNodeValue(val);
        
        return attr;
    }
    
    /*
        <parent>
          <child></child>
        </parent>
    */
    public void appendChild(Element parent, Element child){
        parent.appendChild(child);
    }
}
