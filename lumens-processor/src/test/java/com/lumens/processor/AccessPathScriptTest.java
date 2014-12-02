/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class AccessPathScriptTest {

    public AccessPathScriptTest() {
    }

    @Test
    public void testPathScriptAccessElement() {
        Format person = new DataFormat("Person", Format.Form.STRUCT);
        person.addChild("name", Format.Form.FIELD, Type.STRING);
        Format personAsset = person.addChild("asset", Format.Form.ARRAYOFSTRUCT);
        personAsset.addChild("name", Format.Form.FIELD, Type.STRING);
        personAsset.addChild("price", Format.Form.FIELD, Type.FLOAT);
        Format name = personAsset.addChild("vendor", Format.Form.STRUCT).
        addChild("name", Format.Form.FIELD, Type.STRING);
        assertEquals("Person.asset.vendor.name", name.getFullPath().toString());

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

        Element assetFind = personData.getChildByPath("asset");
        assertEquals(2, assetFind.getChildren().size());

    }
}
