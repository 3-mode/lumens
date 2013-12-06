/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform.serializer;

import com.lumens.io.JsonSerializer;
import com.lumens.io.StringUTF8Writer;
import com.lumens.io.XmlSerializer;
import com.lumens.model.Format;
import com.lumens.processor.transform.TransformRule;
import com.lumens.processor.transform.TransformRuleItem;
import com.lumens.processor.transform.serializer.parser.TransformRuleHandlerImpl;
import com.lumens.processor.transform.serializer.parser.TransformRuleParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.InputSource;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformRuleSerializer implements XmlSerializer, JsonSerializer {

    private TransformRule outputRule;
    private Format ruleFormat;
    private List<TransformRule> unSerializeRuleList;
    private String INDENT_OFFSET = "  ";
    private String INDENT = "";

    public TransformRuleSerializer(TransformRule outputRule) {
        this.outputRule = outputRule;
    }

    public TransformRuleSerializer(Format ruleFormat, List<TransformRule> unserializeRuleList) {
        this.ruleFormat = ruleFormat;
        this.unSerializeRuleList = unserializeRuleList;
    }

    public void initIndent(String indent) {
        this.INDENT = indent;
    }

    @Override
    public void readFromXml(InputStream in) throws Exception {
        TransformRuleParser.parse(new InputSource(in),
                                  new TransformRuleHandlerImpl(ruleFormat, unSerializeRuleList));
    }

    @Override
    public void writeToXml(OutputStream out) throws Exception {
        StringUTF8Writer xml = new StringUTF8Writer(out);
        writeTransformRuleToXml(xml, outputRule, INDENT);
    }

    private void writeTransformRuleToXml(StringUTF8Writer xml, TransformRule rule, String indent) throws IOException {
        xml.print(indent).print("<transform-rule");
        String name = rule.getName();
        if (name != null)
            xml.print(" name=\"").print(name).print("\"");
        xml.println(">");
        writeTransformRuleItemToXml(xml, rule.getRootRuleItem(), indent + INDENT_OFFSET);
        xml.print(indent).println("</transform-rule>");
    }

    private void writeTransformRuleItemToXml(StringUTF8Writer xml, TransformRuleItem ruleItem,
                                             String indent) throws IOException {
        if (ruleItem == null)
            return;

        xml.print(indent).print("<transform-rule-item");
        Format format = ruleItem.getFormat();
        if (format != null)
            xml.print(" format-name=\"").print(format.getName()).print("\"");
        String arrayIterPath = ruleItem.getArrayIterationPath();
        if (arrayIterPath != null)
            xml.print(" array-iteration-path=\"").print(arrayIterPath).print("\"");
        xml.println(">");
        String nextIndent = indent + INDENT_OFFSET;
        String script = ruleItem.getScriptString();
        if (script != null) {
            xml.print(nextIndent).println("<script>");
            xml.println("<![CDATA[");
            xml.println(script);
            xml.println("]]>");
            xml.print(nextIndent).println("</script>");
        }
        Iterator<TransformRuleItem> it = ruleItem.iterator();
        if (it != null)
            while (it.hasNext())
                writeTransformRuleItemToXml(xml, it.next(), nextIndent);
        xml.print(indent).println("</transform-rule-item>");
    }

    @Override
    public void readFromJson(InputStream in) throws Exception {
    }

    @Override
    public void writeToJson(OutputStream out) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonGenerator jGenerator = om.getJsonFactory().createJsonGenerator(out, JsonEncoding.UTF8);
    }
}
