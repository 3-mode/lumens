/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
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

import com.lumens.connector.Direction;
import com.lumens.model.Format;
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
import java.util.Map;

public class DomClient extends XmlClient {
    private DocumentBuilder builder = null;
    protected Document xmlDoc;
    protected Element xmlRoot;  
    
    DomClient(XmlConnector cnt) {
        xmlCntr = cnt;
    }
      
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
    
    @Override
    public void read(InputStream ins)
    {
    
    }
    
    @Override
    public void write(OutputStream ous)
    {
        
    }
    
    @Override
    public boolean validate(String xsdFileName)
    {
        return true;
    }

    public Map<String, Format> getFormatList(Direction direction)
    {
        return null;
    }
    
    public Format getFormat(Format format, String path, Direction direction)
    {
        return null;
    }
}
