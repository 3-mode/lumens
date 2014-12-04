/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import com.lumens.model.Format.Form;
import com.lumens.model.serializer.ElementSerializer;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Iterator;
import javax.xml.bind.JAXBException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ModelTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ModelTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ModelTest.class);
    }

    public void testFormatAndData() throws JAXBException {
        // Create a simple structure, collection and attribute meta data model
        Format root = new DataFormat("root");
        Format person = root.addChild("Person", Form.STRUCT, Type.NONE);
        person.addChild("name", Form.FIELD, Type.STRING);
        Format asset = person.addChild("asset", Form.STRUCT, Type.NONE);
        asset.addChild("name", Form.FIELD, Type.STRING);
        asset.addChild("price", Form.FIELD, Type.FLOAT);
        assertEquals(Type.STRING, person.getChild("asset").getChild("name").
                     getType());

        Element personData = new DataElement(person);
        Element nameData = personData.addChild("name");
        nameData.setValue("James wang");
        assertEquals("James wang", nameData.getValue().getString());
        Element assetData = personData.addChild("asset");
        assetData.addChild("name").setValue("Mac air book");
        assetData.addChild("price").setValue(12000.05f);
        assertEquals(12000.05f, personData.getChild("asset").getChild("price").
                     getValue().getFloat());
    }

    public void testCollection() {
        // Create format
        Format root = new DataFormat("root");
        Format person = root.addChild("Person", Form.STRUCT);
        person.addChild("name", Form.FIELD, Type.STRING);
        Format asset = person.addChild("asset", Form.ARRAYOFSTRUCT);
        asset.addChild("name", Form.FIELD, Type.STRING);
        asset.addChild("price", Form.FIELD, Type.FLOAT);
        asset.addChild("vendor", Form.STRUCT).addChild("name", Form.FIELD,
                                                       Type.STRING);

        // Fill data
        Element personData = new DataElement(person);
        Element nameData = personData.addChild("name");
        nameData.setValue("James wang");
        assertEquals("James wang", nameData.getValue().getString());
        Element assetData = personData.addChild("asset");
        Element assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("name").setValue("Mac air book");
        assetDataItem.addChild("price").setValue(12000.05f);
        assetDataItem.addChild("vendor").addChild("name").setValue("Apple");
        assertEquals(12000.05f, personData.getChild("asset").getChildren().
                     get(0).getChild("price").getValue().getFloat());
    }

    public void testClone() {
        // Create format
        Format root = new DataFormat("root");
        Format person = root.addChild("Person", Form.STRUCT);
        person.addChild("name", Form.FIELD, Type.STRING);
        Format asset = person.addChild("asset", Form.ARRAYOFSTRUCT);
        asset.addChild("name", Form.FIELD, Type.STRING);
        asset.addChild("price", Form.FIELD, Type.FLOAT);
        asset.addChild("vendor", Form.STRUCT).addChild("name", Form.FIELD,
                                                       Type.STRING);

        Format clonedRoot = root.depthCopy();
        assertEquals("name", clonedRoot.getChildByPath("Person.name").getName());
        assertEquals("name", clonedRoot.getChildByPath("Person.asset.vendor.name").getName());
    }

    public void testElementPath() {
        Path path = new AccessPath("asset.vendor.name");
        Iterator<PathToken> it = path.iterator();
        assertEquals("asset", it.next().toString());
        assertEquals("vendor", it.next().toString());
        assertEquals("name", it.next().toString());

        path = new AccessPath("asset.'vendor.info'.name");
        it = path.iterator();
        assertEquals("asset", it.next().toString());
        assertEquals("vendor.info", it.next().toString());
        assertEquals("name", it.next().toString());

        Path removed = path.removeLeft(2);
        assertEquals("name", removed.toString());
        removed = path.removeRight(2);
        assertEquals("asset", removed.toString());

        PathToken token = new PathToken("abc");
        assertTrue(!token.isIndexed());
        assertEquals(-1, token.index());
        token = new PathToken("abc[10]");
        assertEquals("abc", token.toString());
        assertTrue(token.isIndexed());
        assertEquals(10, token.index());

        token = new PathToken("abc10]");
        assertTrue(!token.isIndexed());
        assertEquals(-1, token.index());
    }

    public void testPathForDataElement() throws Exception {
        // Create format
        Format root = new DataFormat("root");
        Format person = root.addChild("Person", Form.STRUCT);
        person.addChild("name", Form.FIELD, Type.STRING);
        Format asset = person.addChild("asset", Form.ARRAYOFSTRUCT);
        asset.addChild("test", Form.ARRAYOFFIELD, Type.STRING);
        asset.addChild("name", Form.FIELD, Type.STRING);
        asset.addChild("price", Form.FIELD, Type.FLOAT);
        Format name = asset.addChild("vendor", Form.STRUCT).
        addChild("name", Form.FIELD, Type.STRING);
        assertEquals("root.Person.asset.vendor.name", name.getFullPath().toString());
        assertEquals(name, root.getChildByPath("Person.asset.vendor.name"));

        // Fill data
        Element personData = new DataElement(person);
        Element nameData = personData.addChild("name");
        nameData.setValue("James wang");
        assertEquals("James wang", nameData.getValue().getString());
        Element assetData = personData.addChild("asset");
        Element assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("test").addArrayItem().setValue("test collection default index");
        assetDataItem.addChild("name").setValue("Mac air book");
        assetDataItem.addChild("price").setValue(12000.05f);
        assetDataItem.addChild("vendor").addChild("name").setValue("Apple");
        assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("name").setValue("HP computer");
        assetDataItem.addChild("price").setValue(15000.05f);
        assetDataItem.addChild("vendor").addChild("name").setValue("HP");

        // if asset is array and asset is not the last path token, the default array item is 0
        // if asset is the last token, return the array element
        assetData = personData.getChildByPath("asset");
        assertNotNull(assetData);
        assertEquals(2, assetData.getChildren().size());
        nameData = personData.getChildByPath("asset[0].vendor.name");
        assertNotNull(nameData);
        nameData = personData.getChildByPath("asset[1].vendor.name");
        assertNotNull(nameData);
        Element test = personData.getChildByPath("asset[0].test[0]");
        assertEquals("test collection default index", test.getValue().getString());

        // test xml
        ElementSerializer serializer = new ElementSerializer(personData, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.writeToXml(baos);
        System.out.println(baos.toString());
        baos.reset();
        serializer.writeToJson(baos);
        System.out.println(baos.toString());
    }

    public void testScriptPath() {
        //var name = @asset.'vendor.info'[0].name+,-,*,/,%,>,<"Hello"
        //var name = @asset.'vendor.info'[0].name; "Hello"
    }

    public void testDateTime() throws ParseException {
        Format dateFormat = new DataFormat("Date", Form.FIELD, Type.DATE);
        Element dateElement = new DataElement(dateFormat);
        dateElement.setValue("2012-04-12T10:22:12Z");
        System.out.println(dateElement.getValue());
        dateElement.setValue("2012-04-12 10:22:12");
        System.out.println(dateElement.getValue());
    }
}
