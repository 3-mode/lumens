/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.model;

import com.lumens.model.serializer.ElementXmlSerializer;
import com.lumens.model.serializer.FormatXmlSerializer;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import junit.framework.TestCase;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ModelSerializeTest extends TestCase
{
    public ModelSerializeTest(String testName)
    {
        super(testName);
    }

    public void testXmlSerializer() throws Exception
    {
        Format root = new DataFormat("root");
        Format person = root.addChild("Person", Format.Form.STRUCT);
        person.setProperty("test1", new Value("prop1"));
        person.setProperty("test2", new Value(119));
        person.setProperty("test3", new Value(true));
        person.setProperty("test4", new Value(3.1415926f));
        person.addChild("name", Format.Form.FIELD, Type.STRING);
        Format asset = person.addChild("asset", Format.Form.ARRAYOFSTRUCT);
        asset.addChild("test", Format.Form.ARRAYOFFIELD, Type.STRING);
        asset.addChild("name", Format.Form.FIELD, Type.STRING);
        asset.addChild("price", Format.Form.FIELD, Type.FLOAT);
        Format name = asset.addChild("vendor", Format.Form.STRUCT).
                addChild("name", Format.Form.FIELD, Type.STRING);

        Element personData = new DataElement(person);
        Element nameData = personData.addChild("name");
        nameData.setValue("James wang");
        assertEquals("James wang", nameData.getValue().getString());
        Element assetData = personData.addChild("asset");
        Element assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("test").addArrayItem().setValue(
                "test collection default index");
        assetDataItem.addChild("name").setValue("Mac air book");
        assetDataItem.addChild("price").setValue(12000.05f);
        assetDataItem.addChild("vendor").addChild("name").setValue("Apple");
        assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("name").setValue("HP computer");
        assetDataItem.addChild("price").setValue(15000.05f);
        assetDataItem.addChild("vendor").addChild("name").setValue("HP");

        ElementXmlSerializer serializer = new ElementXmlSerializer(
                personData, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.writeToXml(baos);
        System.out.println(baos.toString());

    }

    public void testUnSerializeFromXml() throws Exception
    {
        Format root = new DataFormat("Root");
        FormatXmlSerializer xmlSerializer = new FormatXmlSerializer(root);
        InputStream in = null;
        try
        {
            in = ModelSerializeTest.class.getResourceAsStream("/xml/format.xml");
            xmlSerializer.readFromXml(in);
            xmlSerializer.writeToXml(System.out);
        } finally
        {
            in.close();
        }
    }
}
