/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.processor.script.JavaScriptContext;
import com.lumens.processor.transform.TransformForeach;
import com.lumens.processor.transform.TransformMapper;
import com.lumens.processor.transform.TransformRule;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class MapperTest {

    public MapperTest() {
        JavaScriptContext.start();
    }

    @Test
    public void testElementMapping() throws Exception {
        // Build person format
        Format person = new DataFormat("Person", Format.Form.STRUCT);
        person.addChild("name", Format.Form.FIELD, Type.STRING);
        Format personAsset = person.addChild("Asset", Format.Form.ARRAYOFSTRUCT);
        personAsset.addChild("name", Format.Form.FIELD, Type.STRING);
        personAsset.addChild("price", Format.Form.FIELD, Type.FLOAT);
        Format personAssetPart = personAsset.addChild("Part", Format.Form.ARRAYOFSTRUCT);
        personAssetPart.addChild("name", Format.Form.FIELD, Type.STRING);
        personAssetPart.addChild("description", Format.Form.FIELD, Type.STRING);
        Format name = personAsset.addChild("Vendor", Format.Form.STRUCT).addChild("name", Format.Form.FIELD, Type.STRING);
        assertEquals("Person.Asset.Vendor.name", name.getFullPath().toString());

        // Fill person data
        Element personData = new DataElement(person);
        Element nameData = personData.addChild("name");
        nameData.setValue("James wang");
        assertEquals("James wang", nameData.getValue().getString());
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

        // Build transform mapping rule
        TransformRule rule = new TransformRule(wareHouse);

        // Configure For each to iterate the array source elements
        rule.getRuleItem("WareHouse.name").setScript("@Person.name");
        rule.getRuleItem("WareHouse.asset").setTransformForeach(new TransformForeach("Person.Asset", "Asset", "index"));
        rule.getRuleItem("WareHouse.asset.id").setScript("'ASSET_' + index");
        rule.getRuleItem("WareHouse.asset.name").setScript("@Person.Asset[index].name");
        rule.getRuleItem("WareHouse.asset.price").setScript("@Person.Asset[index].price");
        rule.getRuleItem("WareHouse.asset.vendor.name").setScript("@Person.Asset[index].Vendor.name");

        rule.getRuleItem("WareHouse.asset.part").setTransformForeach(new TransformForeach("Person.Asset.Part", "Part", "partIndex"));
        rule.getRuleItem("WareHouse.asset.part.id").setScript("'PART_' + index + '_' + partIndex");
        rule.getRuleItem("WareHouse.asset.part.name").setScript("@Person.Asset[index].Part[partIndex].name");
        //rule.getRuleItem("WareHouse.asset.part.name").setScript("@Asset.Part[partIndex].name");

        List<Element> resultList = null;
        long start = System.currentTimeMillis();
        TransformMapper mapper = new TransformMapper();
        resultList = (List<Element>) mapper.execute(rule, personData);
        System.out.println("Cost: " + (System.currentTimeMillis() - start));

        assertEquals("James wang", resultList.get(0).getChild("name").getValue().getString());
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
    }
}