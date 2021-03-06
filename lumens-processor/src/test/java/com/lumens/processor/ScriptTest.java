/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.CarrierManager;
import com.lumens.processor.transform.MapperContext;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.processor.script.JavaScript;
import com.lumens.processor.script.JavaScriptBuilder;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ScriptTest {

    public ScriptTest() {
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

    public void testJavaScriptMemLeak() throws Exception {
        List<JavaScript> scripts = new ArrayList<>();
        for (int i = 0; i < 50; ++i)
            scripts.add(new JavaScript("function f" + i + "() { var i = now(); return i; }"));
        for (int i = 0; i < 40000; ++i) {
            for (JavaScript script : scripts) {
                Object v = script.execute(new MapperContext() {

                    @Override
                    public Element getRootSourceElement() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public MapperContext getParent() {
                        return null;
                    }

                    @Override
                    public void declareVariables(Scriptable scope) {
                    }

                    @Override
                    public void removeVariables(Scriptable scope) {
                    }

                    @Override
                    public MapperContext getRoot() {
                        return this;
                    }

                    @Override
                    public CarrierManager getCarrierManager() {
                        return null;
                    }

                });
                System.out.println(v);
            }
        }
    }

    @Test
    public void testAddContext() {
        String script = "$GetAccessory(\"Hello\") + $GetAccessory(\"World\")";
        System.out.println(new JavaScriptBuilder().build(script));
    }
}
