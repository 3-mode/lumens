/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.processor.route.RouteProcessor;
import com.lumens.processor.route.RouteRule;
import com.lumens.processor.script.JavaScriptBuilder;
import com.lumens.processor.script.JavaScriptContext;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.io.IOUtils;

/**
 * Unit test for simple App.
 */
public class ProcessorTest
extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ProcessorTest(String testName) {
        super(testName);
        JavaScriptContext.start();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ProcessorTest.class);
    }

    private static InputStream getInputStream(String name) throws Exception {
        return ProcessorTest.class.getClassLoader().getResourceAsStream(name);
    }
    private static Processor transformProcessor = new TransformProcessor();

    public void testTransform() throws Exception {
        // Create format
        Format person = new DataFormat("Person", Form.STRUCT);
        person.addChild("name", Format.Form.FIELD, Type.STRING);
        Format personAsset = person.addChild("asset", Format.Form.ARRAYOFSTRUCT);
        personAsset.addChild("name", Format.Form.FIELD, Type.STRING);
        personAsset.addChild("price", Format.Form.FIELD, Type.FLOAT);
        Format name = personAsset.addChild("vendor", Format.Form.STRUCT).
        addChild("name", Format.Form.FIELD, Type.STRING);
        assertEquals("asset.vendor.name", name.getFullPath().toString());

        // Fill data
        Element personData = new DataElement(person);
        Element nameData = personData.addChild("name");
        nameData.setValue("James wang");
        assertEquals("James wang", nameData.getValue().getString());
        Element assetData = personData.addChild("asset");
        // personAsset item 1
        Element assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("name").setValue("Mac air book");
        assetDataItem.addChild("price").setValue(12000.05f);
        assetDataItem.addChild("vendor").addChild("name").setValue("Apple");
        // personAsset item 2
        assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("name").setValue("HP computer");
        assetDataItem.addChild("price").setValue(15000.05f);
        assetDataItem.addChild("vendor").addChild("name").setValue("HP");

        Format asset = new DataFormat("Asset", Form.STRUCT);
        Format computer = asset.addChild("Computer", Form.ARRAYOFSTRUCT);
        //Format computer = asset.addChild("Computer", Form.STRUCT);
        computer.addChild("name", Form.FIELD, Type.STRING);
        Format cpu = computer.addChild("CPU", Form.ARRAYOFSTRUCT);
        cpu.addChild("name", Form.FIELD, Type.STRING);
        cpu.addChild("corenumber", Form.FIELD, Type.INTEGER);
        cpu.addChild("speed", Form.FIELD, Type.STRING);

        TransformRule rule = new TransformRule(asset);
        rule.getRuleItem("Computer.name").setScript("@asset.name");
        rule.getRuleItem("Computer").setForEachPath("asset");
        assertEquals("@asset.name", rule.getRuleItem("Computer.name").
        getScriptString());

        List<Element> result = (List<Element>) transformProcessor.execute(rule, personData);
        assertEquals("Mac air book", result.get(0).getChildByPath("Computer.name").getValue().getString());
        assertEquals("HP computer", result.get(0).getChildByPath("Computer[1].name").getValue().getString());

        ElementSerializer serializer = new ElementSerializer(result.get(0), true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.writeToXml(baos);
        System.out.println(baos.toString());
    }

    public void testArrayToArray() throws Exception {
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a2, @d-@a4)
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a2)
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@d-@a4)
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a4) (wrong logic, what will happen ?)
        // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (none)
        Format a = new DataFormat("a", Form.STRUCT);
        a.addChild("b", Form.ARRAYOFSTRUCT).addChild("c", Form.STRUCT).addChild("d", Form.ARRAYOFSTRUCT).addChild("e", Form.STRUCT).addChild("f", Form.FIELD, Type.STRING);
        Format a1 = new DataFormat("a1", Form.STRUCT);
        Format a3 = a1.addChild("a2", Form.ARRAYOFSTRUCT).addChild("a3", Form.STRUCT);
        a3.addChild("a4", Form.ARRAYOFSTRUCT).addChild("a5", Form.FIELD, Type.STRING);
        a3.addChild("aa4", Form.ARRAYOFSTRUCT).addChild("aa5", Form.FIELD, Type.STRING);
        // Fill data into a
        Element a_data = new DataElement(a);
        // array b
        Element b_data = a_data.addChild("b");
        Element[] b_data_item = new Element[4];
        b_data_item[0] = b_data.addArrayItem();
        b_data_item[1] = b_data.addArrayItem();
        b_data_item[2] = b_data.addArrayItem();
        b_data_item[3] = b_data.addArrayItem();
        // array d
        for (int i = 0; i < b_data_item.length; ++i) {
            Element d_data = b_data_item[i].addChild("c").addChild("d");
            Element[] d_data_item = new Element[3];
            d_data_item[0] = d_data.addArrayItem();
            d_data_item[1] = d_data.addArrayItem();
            d_data_item[2] = d_data.addArrayItem();
            for (int j = 0; j < d_data_item.length; ++j) {
                d_data_item[j].addChild("e").addChild("f").setValue("test-b[" + i + "]." + "d[" + j + ']');
            }
        }
        ElementSerializer serializer = new ElementSerializer(
        a_data, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.writeToXml(baos);
        System.out.println(baos.toString());
        //(@b-@a2, (@d-@a4, @d-@aa4))
        tryMultipleArrayToArrayTransform(a1, a_data);
        //(@b-@a2)
        tryFirstArrayToFirstArray(a1, a_data);
        //(@d-@a4)
        trySecondArrayToSecondArray(a1, a_data);
        //(@d-a1)
        tryArrayIterationOnRootElement(a1, a_data);

        // check duplicate iteration path
        tryCheckingDuplicateArrayIterationPathConfiguration(a1);

        // Test script
        tryJavaScriptSupportingInTransformRule(a1, a_data);

        // Test router
        tryRouteProcessor(a1, a_data);

    }

    private void tryMultipleArrayToArrayTransform(Format dstFormat, Element data) throws Exception {
        // build the transform rule
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2").setForEachPath("b");
        rule.getRuleItem("a2.a3.a4").setForEachPath("b.c.d");
        rule.getRuleItem("a2.a3.aa4").setForEachPath("b.c.d");

        List<Element> result = (List<Element>) transformProcessor.execute(rule, data);

        ElementSerializer serializer = new ElementSerializer(result.get(0), true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.writeToXml(baos);
        System.out.println("tryMultipleArrayToArrayTransform####\n" + baos.toString());

        assertEquals("test-b[3].d[2]", result.get(0).getChildByPath("a2[3].a3.a4[2].a5").getValue().getString());
        assertEquals("test-b[3].d[2]", result.get(0).getChildByPath("a2[3].a3.aa4[2].aa5").getValue().getString());
    }

    private void tryFirstArrayToFirstArray(Format dstFormat, Element data) throws Exception {
        // build the transform rule
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2").setForEachPath("b");
        List<Element> result = (List<Element>) transformProcessor.execute(rule, data);

        ElementSerializer serializer = new ElementSerializer(result.
        get(0), true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.writeToXml(baos);
        System.out.println("tryFirstArrayToFirstArray####\n" + baos.toString());

        assertEquals("test-b[3].d[0]", result.get(0).getChildByPath("a2[3].a3.a4[0].a5").getValue().getString());
    }

    private void trySecondArrayToSecondArray(Format dstFormat, Element data) throws Exception {
        // build the transform rule
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.a4").setForEachPath("b.c.d");
        List<Element> result = (List<Element>) transformProcessor.execute(rule, data);

        ElementSerializer serializer = new ElementSerializer(result.
        get(0), true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.writeToXml(baos);
        System.out.
        println("trySecondArrayToSecondArray####\n" + baos.toString());

        assertEquals("test-b[1].d[0]", result.get(0).getChildByPath(
        "a2[0].a3.a4[3].a5").getValue().getString());
    }

    private void tryArrayIterationOnRootElement(Format dstFormat, Element data) throws Exception {
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRootRuleItem().setForEachPath("b.c.d");
        List<Element> result = (List<Element>) transformProcessor.execute(rule, data);

        for (Element elem : result) {
            ElementSerializer serializer = new ElementSerializer(
            elem, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            serializer.writeToXml(baos);
            System.out.println("tryArrayIterationOnRootElement####\n" + baos.
            toString());
        }
        assertEquals("test-b[3].d[2]", result.get(11).getChildByPath("a2.a3.a4.a5").getValue().getString());
    }

    private void tryCheckingDuplicateArrayIterationPathConfiguration(Format dstFormat) throws Exception {
        // build the transform rule
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2").setForEachPath("b");
        rule.getRuleItem("a2.a3.a4").setForEachPath("b.c.d");
        rule.getRuleItem("a2.a3.aa4").setForEachPath("b.c.d");
        try {
            // It will be error, "b.c.d" is used in child element
            rule.getRuleItem("a2").setForEachPath("b.c.d");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rule.getRuleItem("a2.a3.a4").setForEachPath(null);
            rule.getRuleItem("a2.a3.aa4").setForEachPath(null);
            rule.getRuleItem("a2").setForEachPath("b.c.d");
        }

        try {
            // It will be error, "b.c.d" is used in parent element
            rule.getRuleItem("a2.a3.a4").setForEachPath("b.c.d");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void testJavaScriptBuilder() throws Exception {
        JavaScriptBuilder builder = new JavaScriptBuilder();
        String script = builder.build(IOUtils.toString(getInputStream("test-script/transform-script-test.txt")));
        System.out.println("testJavaScriptBuilder###: " + script);
        String s = "@'a.b.c./a/*'";
        script = builder.build(s);
        System.out.println("testJavaScriptBuilder###: " + script);
        assertTrue(script.contains("getElementValue(ctx, \"'a.b.c./a/*'"));
    }

    public void tryJavaScriptSupportingInTransformRule(Format dstFormat, Element data) throws Exception {
        TransformRule rule = new TransformRule(dstFormat);
        rule.getRuleItem("a2.a3.a4.a5").setScript("@b.c.d.e.f");
        rule.getRuleItem("a2.a3.aa4.aa5").setScript(IOUtils.toString(getInputStream("test-script/aa5-script.txt")));
        rule.getRuleItem("a2").setForEachPath("b");
        rule.getRuleItem("a2.a3.a4").setForEachPath("b.c.d");
        rule.getRuleItem("a2.a3.aa4").setForEachPath("b.c.d");
        List<Element> result = (List<Element>) transformProcessor.execute(rule, data);

        ElementSerializer serializer = new ElementSerializer(result.get(0), true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.writeToXml(baos);
        System.out.println("tryJavaScriptSupportingInTransformRule####:\n" + baos.toString());

        assertEquals("test-b[3].d[2]", result.get(0).getChildByPath("a2[3].a3.a4[2].a5").getValue().getString());
        assertEquals("test-b[3].d[2]test test again", result.get(0).getChildByPath("a2[3].a3.aa4[2].aa5").getValue().getString());
    }

    public void tryRouteProcessor(Format dstFormat, Element data) throws Exception {
        RouteProcessor router = new RouteProcessor();
        Processor t1 = new TransformProcessor();
        router.addRoutePoint(t1, new RouteRule("scipt here"));
        Processor t2 = new TransformProcessor();
        router.addRoutePoint(t2, new RouteRule("scipt here"));
        //router.execute(data);
    }
}
