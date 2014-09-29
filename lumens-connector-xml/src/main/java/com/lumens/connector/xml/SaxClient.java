/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import com.lumens.connector.Direction;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.serializer.FormatSerializer;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.SAXValidator;
import org.dom4j.DocumentException;
import org.dom4j.util.XMLErrorHandler;

import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;

import java.io.File;
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;  
import java.util.HashMap;
import java.util.Map;
  
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

/*
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.transform.OutputKeys;  
import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerConfigurationException;  
import javax.xml.transform.TransformerException;  
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.stream.StreamResult; 
*/
/**
 *
 * @author whiskey
 */
public class SaxClient extends XmlClient{   
    private SAXReader xmlReader; 
    private SAXParser saxParser;
    private Document xmlDoc;
    private Element xmlRoot;  
    
    SaxClient(XmlConnector cnt) {
        xmlCntr = cnt;
    }
    
    @Override
    public void init()
    {
        if (null == saxParser)
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            try
            {
                saxParser = factory.newSAXParser();            
            }catch(ParserConfigurationException e)
            {               
                e.printStackTrace();
            }catch(SAXException e)
            {
                e.printStackTrace();
            }
        }
        if (null == xmlReader)
        {
            xmlReader = new SAXReader();
        }
    }
    
    @Override
    public void read(InputStream ins)
    {
        init();
        
        try{
            xmlDoc = xmlReader.read(ins); 
            xmlRoot = xmlDoc.getRootElement();
        }catch(DocumentException  e)
        {
            e.printStackTrace();
        }
    }
        
    @Override
    public void write(OutputStream ous)
    {
        init();        
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction)
    {        
        Map<String, Format> fmtMap = new HashMap<>();  

        for ( Iterator itor = xmlRoot.elementIterator(); itor.hasNext();)
        {   
            Element elem = (Element)itor.next();
            String name = elem.getQName().getName();
            Format fmt = new DataFormat(name, Format.Form.STRUCT);        
            fmt.addChild("Fields", Format.Form.STRUCT);          
            
            fmtMap.put(name, fmt);
        }
       
        return fmtMap;        
    }
    
    @Override
    public Format getFormat(Format format, String path, Direction direction)    
    {
        return null;
    }
    
    @Override
    public boolean validate(String xsdFileName)
    {  
        XMLErrorHandler errHandler =  new XMLErrorHandler();
      
        try{
            saxParser.setProperty( 
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
                    "http://www.w3.org/2001/XMLSchema"); 
            saxParser.setProperty( 
                    "http://java.sun.com/xml/jaxp/properties/schemaSource", 
                    "file:" + xsdFileName);   
        
            SAXValidator validator = new SAXValidator(xmlReader.getXMLReader());
            validator.setErrorHandler(errHandler);
            validator.validate(xmlDoc);
        }catch(SAXNotRecognizedException e)
        {
           e.printStackTrace();
        }catch(SAXException e)
        {
            e.printStackTrace();
        }
        
        return errHandler.getErrors().hasContent();
    }        
}
