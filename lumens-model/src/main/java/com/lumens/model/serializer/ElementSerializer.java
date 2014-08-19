/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model.serializer;

import com.lumens.io.StringUTF8Writer;
import com.lumens.io.XmlSerializer;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author shaofeng wang
 */
public class ElementSerializer implements XmlSerializer {

    private Element element;
    private boolean useIndent;
    private String INDENT = "  ";

    public ElementSerializer(Element element, boolean indent) {
        this.element = element;
        this.useIndent = indent;
    }

    public void initIndent(String indent) {
        this.INDENT = indent;
    }

    @Override
    public void readFromXml(InputStream in) throws Exception {
    }

    @Override
    public void writeToXml(OutputStream out) throws Exception {
        StringUTF8Writer dataOut = new StringUTF8Writer(out);
        writeElementToXml(element, "", dataOut);
    }

    private void writeElementToXml(Element element, String indent, StringUTF8Writer out) throws Exception {
        boolean closeTag = false;
        Format format = element.getFormat();
        out.print(indent)
        .print("<element name=\"").print(format.getName()).print("\" ")
        .print("form=\"").print(format.getForm().toString()).print("\" ").print("type=\"").print(format.getType().toString()).print("\"");
        if (!element.isArray() && format.getType() != Type.NONE && !element.isNull()) {
            if (!closeTag)
                out.print(">");
            closeTag = true;
            if (element.isField() || element.getChildren() == null)
                out.print(element.getValue().getString());
            else
                out.println(element.getValue().getString());
        }

        List<Element> children = element.getChildren();
        if (children != null && children.size() > 0) {
            if (!closeTag)
                out.println(">");
            closeTag = true;
            for (Element child : children)
                writeElementToXml(child, indent + INDENT, out);
        }
        if (closeTag) {
            if (element.isField() || element.getChildren() == null)
                out.println("</element>");
            else
                out.print(indent.toString()).println("</element>");
        } else {
            out.println("/>");
        }
    }

    public void writeToJson(OutputStream out) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonGenerator jGenerator = om.getJsonFactory().createJsonGenerator(out, JsonEncoding.UTF8);
        writeRootElementToJson(element, jGenerator);
        jGenerator.flush();
    }

    public void writeToJson(JsonGenerator jGenerator) throws Exception {
        jGenerator.writeObjectFieldStart("element");
        writeElementToJson(element, jGenerator);
        jGenerator.writeEndObject();
        jGenerator.flush();
    }

    private void writeRootElementToJson(Element element, JsonGenerator jGenerator) throws IOException {
        jGenerator.writeStartObject();
        jGenerator.writeObjectFieldStart("element");
        jGenerator.writeStringField("name", element.getFormat().getName());
        jGenerator.writeStringField("form", element.getFormat().getForm().toString());
        jGenerator.writeStringField("type", element.getFormat().getType().toString());
        if (element.getValue() != null)
            jGenerator.writeStringField("value", element.getValue().getString());
        if (element.getChildren() != null) {
            jGenerator.writeArrayFieldStart("element");
            for (Element child : element.getChildren())
                writeElementToJson(child, jGenerator);
            jGenerator.writeEndArray();
        }
        jGenerator.writeEndObject();
        jGenerator.writeEndObject();
    }

    private void writeElementToJson(Element element, JsonGenerator jGenerator) throws IOException {
        jGenerator.writeStartObject();
        jGenerator.writeStringField("name", element.getFormat().getName());
        jGenerator.writeStringField("form", element.getFormat().getForm().toString());
        jGenerator.writeStringField("type", element.getFormat().getType().toString());
        if (element.getValue() != null)
            jGenerator.writeStringField("value", element.getValue().getString());
        if (element.getChildren() != null) {
            jGenerator.writeArrayFieldStart("element");
            for (Element child : element.getChildren())
                writeElementToJson(child, jGenerator);
            jGenerator.writeEndArray();
        }
        jGenerator.writeEndObject();
    }
}