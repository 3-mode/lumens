/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.processor;

import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.transform.TransformForeach;
import com.lumens.processor.transform.TransformRule;
import com.lumens.processor.transform.serializer.TransformRuleSerializer;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformRuleTest extends TestCase {

    public TransformRuleTest(String testName) {
        super(testName);
    }

    public void testReadTransformRuleFromXml() throws Exception {
        Format root = new DataFormat("Root");
        try (InputStream in = TransformRuleTest.class.getResourceAsStream("/xml/WareHouse_Format.xml")) {
            FormatSerializer xmlSerializer = new FormatSerializer(root);
            xmlSerializer.readFromXml(in);
        }
        Format warehouse = root.getChild("WareHouse");
        warehouse.setParent(null);

        TransformRule rule = new TransformRule(warehouse);
        //******* Test the array mapping when the parent and the current for each are configured.
        // Configure For each to iterate the array source elements
        rule.getRuleItem("WareHouse.name").setScript("@Person.name");
        rule.getRuleItem("WareHouse.asset").addTransformForeach(new TransformForeach("Person.Asset", "Asset", "index"));
        rule.getRuleItem("WareHouse.asset.id").setScript("'ASSET_' + index");
        rule.getRuleItem("WareHouse.asset.name").setScript("@Person.Asset[index].name");
        rule.getRuleItem("WareHouse.asset.price").setScript("@Person.Asset[index].price");
        rule.getRuleItem("WareHouse.asset.vendor.name").setScript("@Person.Asset[index].Vendor.name");

        rule.getRuleItem("WareHouse.asset.part").addTransformForeach(new TransformForeach("Person.Asset", "Asset", "index2"));
        rule.getRuleItem("WareHouse.asset.part").addTransformForeach(new TransformForeach("Person.Asset.Part", "Part", "partIndex"));
        rule.getRuleItem("WareHouse.asset.part.id").setScript("'PART_' + index2 + '_' + partIndex");
        rule.getRuleItem("WareHouse.asset.part.name").setScript("@Person.Asset[index2].Part[partIndex].name");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TransformRuleSerializer ruleSerial = new TransformRuleSerializer(rule);
        ruleSerial.writeToJson(baos);
        System.out.println("**********Json project*****************************************");
        System.out.println(baos.toString());
        baos.reset();
        System.out.println("**********Xml project*****************************************");
        ruleSerial.writeToXml(baos);
        String backupXml = baos.toString();
        System.out.println(backupXml);

        List<TransformRule> unserializeRuleList = new ArrayList<>();
        TransformRuleSerializer ruleSerialRead = new TransformRuleSerializer(warehouse, unserializeRuleList);
        try (InputStream in = TransformRuleTest.class.getResourceAsStream("/xml/WarehouseTransformRule.xml")) {
            ruleSerialRead.readFromXml(in);
        }
        TransformRuleSerializer outputAgain = new TransformRuleSerializer(unserializeRuleList.get(0));
        baos.reset();
        System.out.println("**********Xml project 2*****************************************");
        ruleSerial.writeToXml(baos);
        String backupXml2 = baos.toString();
        System.out.println(backupXml2);
        assertEquals(backupXml, backupXml2);
    }

    @Test
    public void testScript() throws Exception {
        String script = "@fields.CITY + 'Test'";
        assertFalse(ProcessorUtils.isPathFormat(script));
    }
}
