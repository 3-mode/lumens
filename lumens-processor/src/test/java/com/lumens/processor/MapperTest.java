/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.transform.TransformForeach;
import com.lumens.processor.transform.TransformMapper;
import com.lumens.processor.transform.TransformRule;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class MapperTest {

    @Test
    public void testElementMapping() throws Exception {

        // Build person format
        Format root = new DataFormat("Root");
        try (InputStream in = MapperTest.class.getResourceAsStream("/xml/Person_Format.xml")) {
            FormatSerializer xmlSerializer = new FormatSerializer(root);
            xmlSerializer.readFromXml(in);
        }
        Format person = root.getChild("Person");
        person.setParent(null);
        assertEquals("Person.Asset.Vendor.name", person.getChildByPath("Asset.Vendor.name").getFullPath().toString());

        // Fill person data
        Element personData = new DataElement(person);
        try (InputStream in = MapperTest.class.getResourceAsStream("/xml/Person_Element.xml")) {
            ElementSerializer xmlSerializer = new ElementSerializer(personData, true);
            xmlSerializer.readFromXml(in);
        }

        buildPersonData(personData);

        Element assetFind = personData.getChildByPath("Asset");
        assertEquals(2, assetFind.getChildren().size());

        // Build warehouse format
        Format wareHouse = new DataFormat("WareHouse", Format.Form.STRUCT);
        wareHouse.addChild("name", Format.Form.FIELD, Type.STRING);
        Format instockAsset = wareHouse.addChild("asset", Format.Form.ARRAYOFSTRUCT);
        instockAsset.addChild("id", Format.Form.FIELD, Type.STRING);
        instockAsset.addChild("name", Format.Form.FIELD, Type.STRING);
        instockAsset.addChild("price", Format.Form.FIELD, Type.FLOAT);
        Format venderName = instockAsset.addChild("vendor", Format.Form.STRUCT).addChild("name", Format.Form.FIELD, Type.STRING);

        Format assetPart = instockAsset.addChild("part", Format.Form.ARRAYOFSTRUCT);
        assetPart.addChild("id", Format.Form.FIELD, Type.STRING);
        assetPart.addChild("name", Format.Form.FIELD, Type.STRING);
        assetPart.addChild("description", Format.Form.FIELD, Type.STRING);
        FormatSerializer wareHourseWriter = new FormatSerializer(wareHouse);
        wareHourseWriter.writeToXml(System.out);

        // Build transform mapping rule
        TransformRule rule = new TransformRule(wareHouse);

        //******* Test the array mapping when the parent and the current for each are configured.
        // Configure For each to iterate the array source elements
        rule.getRuleItem("WareHouse.name").setScript("@Person.name");
        rule.getRuleItem("WareHouse.asset").addTransformForeach(new TransformForeach("Person.Asset", "Asset", "index"));
        rule.getRuleItem("WareHouse.asset.id").setScript("'ASSET_' + index");
        rule.getRuleItem("WareHouse.asset.name").setScript("@Person.Asset[index].name");
        rule.getRuleItem("WareHouse.asset.price").setScript("@Person.Asset[index].price");
        rule.getRuleItem("WareHouse.asset.vendor.name").setScript("@Person.Asset[index].Vendor.name");

        rule.getRuleItem("WareHouse.asset.part").addTransformForeach(new TransformForeach("Person.Asset.Part", "Part", "partIndex"));
        rule.getRuleItem("WareHouse.asset.part.id").setScript("'PART_' + index + '_' + partIndex");
        rule.getRuleItem("WareHouse.asset.part.name").setScript("@Person.Asset[index].Part[partIndex].name");
        //rule.getRuleItem("WareHouse.asset.part.name").setScript("@Asset.Part[partIndex].name");

        List<Element> resultList = null;
        long start = System.currentTimeMillis();
        TransformMapper mapper = new TransformMapper();
        resultList = (List<Element>) mapper.execute(new ProcessorExecutionContext(rule, Arrays.asList(personData)));
        System.out.println("Cost: " + (System.currentTimeMillis() - start));

        //assertEquals("James wang", resultList.get(0).getChild("name").getValue().getString());
        assertEquals(1, resultList.size());
        List<Element> assetItems = resultList.get(0).getChildByPath("asset").getChildren();
        assertEquals(2, assetItems.size());
        assertEquals("ASSET_0", assetItems.get(0).getChildByPath("id").getValue().getString());
        assertEquals("Mac air book", assetItems.get(0).getChildByPath("name").getValue().getString());
        assertEquals(12000.05f, assetItems.get(0).getChildByPath("price").getValue().getFloat());
        assertEquals("Apple", assetItems.get(0).getChildByPath("vendor.name").getValue().getString());
        List<Element> parts = assetItems.get(0).getChildByPath("part").getChildren();
        assertEquals(3, parts.size());
        assertEquals("PART_0_0", parts.get(0).getChild("id").getValue().getString());
        assertEquals("PART_0_1", parts.get(1).getChild("id").getValue().getString());
        assertEquals("PART_0_2", parts.get(2).getChild("id").getValue().getString());

        assertEquals("ASSET_1", assetItems.get(1).getChildByPath("id").getValue().getString());
        assertEquals("HP computer", assetItems.get(1).getChildByPath("name").getValue().getString());
        assertEquals(15000.05f, assetItems.get(1).getChildByPath("price").getValue().getFloat());
        assertEquals("HP", assetItems.get(1).getChildByPath("vendor.name").getValue().getString());
        parts = assetItems.get(1).getChildByPath("part").getChildren();
        assertEquals(2, parts.size());
        assertEquals("PART_1_0", parts.get(0).getChild("id").getValue().getString());
        assertEquals("PART_1_1", parts.get(1).getChild("id").getValue().getString());

        // Test ArrayOfField
        Format finalRoot = new DataFormat("Root");
        try (InputStream in = MapperTest.class.getResourceAsStream("/xml/Final_Format.xml")) {
            FormatSerializer xmlSerializer = new FormatSerializer(finalRoot);
            xmlSerializer.readFromXml(in);
        }
        Format finalFmt = finalRoot.getChild("Final");
        finalFmt.setParent(null);
        TransformRule rule_Final = new TransformRule(finalFmt);
        rule_Final.getRuleItem("Final.name").setScript("@WareHouse.name");
        rule_Final.getRuleItem("Final.value").addTransformForeach(new TransformForeach("WareHouse.asset", "Asset", "index"));
        rule_Final.getRuleItem("Final.value").setScript("@WareHouse.asset[index].id");
        rule_Final.getRuleItem("Final.Vendor.value").addTransformForeach(new TransformForeach("WareHouse.asset", "Asset", "index"));
        rule_Final.getRuleItem("Final.Vendor.value").setScript("@WareHouse.asset[index].id");
        resultList = (List<Element>) mapper.execute(new ProcessorExecutionContext(rule_Final, resultList));
        assertEquals(1, resultList.size());
        assertEquals(2, resultList.get(0).getChildByPath("value").getChildren().size());
        assertEquals("ASSET_0", resultList.get(0).getChildByPath("value[0]").getValue().getString());
        assertEquals("ASSET_1", resultList.get(0).getChildByPath("value[1]").getValue().getString());
        assertEquals(2, resultList.get(0).getChildByPath("Vendor.value").getChildren().size());
        assertEquals("ASSET_0", resultList.get(0).getChildByPath("Vendor.value[0]").getValue().getString());
        assertEquals("ASSET_1", resultList.get(0).getChildByPath("Vendor.value[1]").getValue().getString());

        //******* Test the root has a for each configruation to create root element result list
        Format departRoot = new DataFormat("Root");
        try (InputStream in = MapperTest.class.getResourceAsStream("/xml/Department_Format.xml")) {
            FormatSerializer xmlSerializer = new FormatSerializer(departRoot);
            xmlSerializer.readFromXml(in);
        }
        Format department = departRoot.getChild("Department");
        department.setParent(null);
        Element departmentData = new DataElement(department);

        Element personElem = departmentData.addChild("Person");
        buildPersonData(personElem.addArrayItem());
        buildPersonData(personElem.addArrayItem());
        // Configure For each to iterate the array source elements
        TransformRule rule_For_ListResult = new TransformRule(wareHouse);
        rule_For_ListResult.getRuleItem("WareHouse").addTransformForeach(new TransformForeach("Department.Person", "Asset", "index"));
        rule_For_ListResult.getRuleItem("WareHouse.name").setScript("@Department.Person[index].name");
        rule_For_ListResult.getRuleItem("WareHouse.asset").addTransformForeach(new TransformForeach("Department.Person.Asset", "Asset", "indexAsset"));
        rule_For_ListResult.getRuleItem("WareHouse.asset.id").setScript("'ASSET_' + index + '_' + indexAsset");
        rule_For_ListResult.getRuleItem("WareHouse.asset.name").setScript("@Department.Person[index].Asset[indexAsset].name");
        rule_For_ListResult.getRuleItem("WareHouse.asset.price").setScript("@Department.Person[index].Asset[indexAsset].price");
        rule_For_ListResult.getRuleItem("WareHouse.asset.vendor.name").setScript("@Department.Person[index].Asset[indexAsset].Vendor.name");

        resultList = (List<Element>) mapper.execute(new ProcessorExecutionContext(rule_For_ListResult, Arrays.asList(departmentData)));

        assertEquals(2, resultList.size());

        //******* TODO Testing when a parent array without for each configuration and the current array has for each configuration
        //******* TODO on an array element conifgure for each list
        TransformRule rule_For_ForeachListResult = new TransformRule(wareHouse);
        rule_For_ForeachListResult.getRuleItem("WareHouse.name").setScript("@Department.Person[0].name");
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset").addTransformForeach(new TransformForeach("Department.Person", "Person", "index"));
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset").addTransformForeach(new TransformForeach("Department.Person.Asset", "Asset", "indexAsset"));
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset.id").setScript("var id = 'ASSET_' + index + '_' + indexAsset; \n $LogInfo(id); \n return id;");
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset.name").setScript("@Department.Person[index].Asset[indexAsset].name");
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset.price").setScript("@Department.Person[index].Asset[indexAsset].price");
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset.vendor.name").setScript("@Department.Person[index].Asset[indexAsset].Vendor.name");
        start = System.currentTimeMillis();
        resultList = (List<Element>) mapper.execute(new ProcessorExecutionContext(rule_For_ForeachListResult, Arrays.asList(departmentData)));
        System.out.println("Cost: " + (System.currentTimeMillis() - start));

        // Test multiple for each on root element
        rule_For_ForeachListResult = new TransformRule(wareHouse);
        rule_For_ForeachListResult.getRuleItem("WareHouse").addTransformForeach(new TransformForeach("Department.Person", "Person", "index"));
        rule_For_ForeachListResult.getRuleItem("WareHouse").addTransformForeach(new TransformForeach("Department.Person.Asset", "Asset", "indexAsset"));
        rule_For_ForeachListResult.getRuleItem("WareHouse.name").setScript("@Department.Person[0].name");
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset.id").setScript("var id = 'ASSET_' + index + '_' + indexAsset; \n $LogInfo(id); \n return id;");
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset.name").setScript("@Department.Person[0].Asset[0].name");
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset.price").setScript("@Department.Person[0].Asset[0].price");
        rule_For_ForeachListResult.getRuleItem("WareHouse.asset.vendor.name").setScript("@Department.Person[0].Asset[0].Vendor.name");
        start = System.currentTimeMillis();
        resultList = (List<Element>) mapper.execute(new ProcessorExecutionContext(rule_For_ForeachListResult, Arrays.asList(departmentData)));
        System.out.println("Cost: " + (System.currentTimeMillis() - start));
        assertEquals(4, resultList.size());
        assertEquals(departmentData.getChildByPath("Person[0].Asset[0].name").getValue().getString(),
                     resultList.get(0).getChildByPath("asset[0].name").getValue().getString());
        assertEquals("ASSET_1_1", resultList.get(3).getChildByPath("asset[0].id").getValue().getString());
    }

    private void buildPersonData(Element personData) {
        Element nameData = personData.addChild("name");
        long mills = System.currentTimeMillis();
        nameData.setValue("James wang_" + mills);
        assertEquals("James wang_" + mills, nameData.getValue().getString());
        Element assetData = personData.addChild("Asset");
        // personAsset item 1
        Element assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("name").setValue("Mac air book");
        assetDataItem.addChild("price").setValue(12000.05f);
        assetDataItem.addChild("Vendor").addChild("name").setValue("Apple");
        Element assetItemPart = assetDataItem.addChild("Part");
        assetItemPart.addArrayItem().addChild("name").setValue("Mac mouse");
        assetItemPart.addArrayItem().addChild("name").setValue("Mac keyboard");
        assetItemPart.addArrayItem().addChild("name").setValue("Mac voice box");
        // personAsset item 2
        assetDataItem = assetData.addArrayItem();
        assetDataItem.addChild("name").setValue("HP computer");
        assetDataItem.addChild("price").setValue(15000.05f);
        assetDataItem.addChild("Vendor").addChild("name").setValue("HP");
        assetItemPart = assetDataItem.addChild("Part");
        assetItemPart.addArrayItem().addChild("name").setValue("HP mouse");
        assetItemPart.addArrayItem().addChild("name").setValue("HP keyboard");
    }
}
