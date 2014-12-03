/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.processor;

import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.processor.script.JavaScriptContext;
import com.lumens.processor.transform.TransformRule;
import com.lumens.processor.transform.serializer.TransformRuleSerializer;
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
        InputStream in = null;
        try {
            // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a2, @d-@a4)
            // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a2)
            // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@d-@a4)
            // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (@b-@a4) (wrong logic, what will happen ?)
            // a.@b.c.@d.e.f --> a1.@a2.a3.@a4.a5 (none)
            Format a = new DataFormat("a", Format.Form.STRUCT);
            a.addChild("b", Format.Form.ARRAYOFSTRUCT).addChild("c", Format.Form.STRUCT).addChild("d", Format.Form.ARRAYOFSTRUCT).addChild("e", Format.Form.STRUCT).addChild("f", Format.Form.FIELD, Type.STRING);
            Format a1 = new DataFormat("a1", Format.Form.STRUCT);
            Format a3 = a1.addChild("a2", Format.Form.ARRAYOFSTRUCT).addChild("a3", Format.Form.STRUCT);
            a3.addChild("a4", Format.Form.ARRAYOFSTRUCT).addChild("a5", Format.Form.FIELD, Type.STRING);
            a3.addChild("aa4", Format.Form.ARRAYOFSTRUCT).addChild("aa5", Format.Form.FIELD, Type.STRING);

            List<TransformRule> ruleList = new ArrayList<>();
            in = TransformRuleTest.class.getResourceAsStream("/xml/transform-rule.xml");
            TransformRuleSerializer unSerialXml = new TransformRuleSerializer(a1, ruleList);
            unSerialXml.readFromXml(in);
            TransformRuleSerializer xml = new TransformRuleSerializer(ruleList.get(0));
            System.out.println("Print the rule structure after read it from a xml : ");
            xml.writeToXml(System.out);
        } finally {
            in.close();
        }
    }

    @Test
    public void testScript() throws Exception {
        String script = "@fields.CITY + 'Test'";
        assertFalse(ProcessorUtils.isPathFormat(script));
    }
}
