/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.serializer;

import com.lumens.connector.Direction;
import com.lumens.engine.StartEntry;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.component.instrument.DataTransformator;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.engine.serializer.parser.ProjectHandlerImpl;
import com.lumens.engine.serializer.parser.ProjectParser;
import com.lumens.io.JsonSerializer;
import com.lumens.io.StringUTF8Writer;
import com.lumens.io.XmlSerializer;
import com.lumens.model.Format;
import com.lumens.model.Value;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.transform.TransformRule;
import com.lumens.processor.transform.serializer.TransformRuleSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.InputSource;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ProjectSerializer implements XmlSerializer, JsonSerializer {

    private final static String INDENT = "  ";
    private TransformProject project;

    public ProjectSerializer(TransformProject project) {
        this.project = project;
    }

    @Override
    public void readFromXml(InputStream in) throws Exception {
        ProjectParser.parse(new InputSource(in), new ProjectHandlerImpl(project));
    }

    @Override
    public void readFromJson(InputStream in) throws Exception {
        new ProjectJsonParser(project).parse(in);
    }

    public void readFromJson(JsonNode projectRootJson) throws Exception {
        new ProjectJsonParser(project).parse(projectRootJson);
    }

    @Override
    public void writeToJson(OutputStream out) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonGenerator jGenerator = om.getJsonFactory().createJsonGenerator(out, JsonEncoding.UTF8);
        jGenerator.writeStartObject();
        jGenerator.writeObjectFieldStart("project");
        jGenerator.writeStringField("name", project.getName());
        jGenerator.writeStringField("description", project.getDescription());
        writeDatasourceListToJson(jGenerator, project.getDatasourceList());
        writeDataTransformatorListToJson(jGenerator, project.getDataTransformatorList());
        writeStartEntryListToJson(jGenerator, project.getStartEntryList());
        jGenerator.writeEndObject();
        jGenerator.writeEndObject();
        jGenerator.flush();
    }

    @Override
    public void writeToXml(OutputStream out) throws Exception {
        StringUTF8Writer xml = new StringUTF8Writer(out);
        xml.print("<project").print(" name=\"").print(project.getName()).println("\">");
        xml.print(INDENT)
        .println("<description>")
        .print("<![CDATA[").print(project.getDescription()).println("]]>").print(INDENT)
        .println("</description>");
        writeDatasourceListToXml(xml, project.getDatasourceList(), INDENT);
        writeDataTransformatorListToXml(xml, project.getDataTransformatorList(), INDENT);
        writeStartEntryListToXml(xml, project.getStartEntryList(), INDENT);
        xml.println("</project>");
    }

    private void writeDatasourceListToXml(StringUTF8Writer xml, List<DataSource> datasourceList, String indent) throws Exception {
        xml.print(indent).println("<resource-list>");
        if (datasourceList != null && !datasourceList.isEmpty()) {
            for (DataSource ds : datasourceList)
                writeDatasourceToXml(xml, ds, indent + INDENT);
        }
        xml.print(indent).println("</resource-list>");
    }

    private void writeDatasourceToXml(StringUTF8Writer xml, DataSource ds, String indent) throws Exception {
        String nextIndent = indent + INDENT;
        xml.print(indent)
        .print("<datasource type=\"").print(ds.getComponentType()).print("\" id=\"").print(ds.getId()).print("\" name=\"").print(ds.getName() == null ? "" : ds.getName()).println("\">");
        xml.print(nextIndent)
        .println("<description>")
        .print("<![CDATA[").print(ds.getDescription() == null ? "" : ds.getDescription()).println("]]>").print(nextIndent)
        .println("</description>");
        xml.print(nextIndent)
        .print("<position x=\"").print(Integer.toString(ds.getX())).print("\" y=\"").print(Integer.toString(ds.getY())).println("\"/>");
        writeDatasourceParameterListToXml(xml, ds.getPropertyList(), nextIndent);
        writeRegisterFormatListToXml(xml, Direction.IN, ds.getRegisteredFormatList(Direction.IN), nextIndent);
        writeRegisterFormatListToXml(xml, Direction.OUT, ds.getRegisteredFormatList(Direction.OUT), nextIndent);
        writeTargetListToXml(xml, ds.getTargetList(), nextIndent);
        xml.print(indent)
        .println("</datasource>");
    }

    private void writeTargetListToXml(StringUTF8Writer xml, Map<String, TransformComponent> targetList, String indent) throws IOException {
        xml.print(indent)
        .println("<target-list>");
        if (targetList != null) {
            String nextIndent = indent + INDENT;
            Iterator<Entry<String, TransformComponent>> it = targetList.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, TransformComponent> entry = it.next();
                xml.print(nextIndent).print("<target ").print("id=\"").print(entry.getKey()).println("\"/>");
            }
        }
        xml.print(indent)
        .println("</target-list>");
    }

    private void writeDatasourceParameterListToXml(StringUTF8Writer xml, Map<String, Value> propertyList, String indent) throws IOException {
        xml.print(indent)
        .println("<property-list>");
        if (propertyList != null && !propertyList.isEmpty()) {
            String nextIndent = indent + INDENT;
            Iterator<Entry<String, Value>> it = propertyList.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Value> entry = it.next();
                xml.print(nextIndent)
                .print("<property name=\"").print(entry.getKey()).print("\"");
                xml.print(" type=\"").print(entry.getValue().type().toString()).print("\">");
                xml.print(entry.getValue().toString());
                xml.println("</property>");
            }
        }
        xml.print(indent)
        .println("</property-list>");
    }

    private void writeRegisterFormatListToXml(StringUTF8Writer xml, Direction direction, Map<String, FormatEntry> registeredFormatList, String indent) throws Exception {
        xml.print(indent)
        .print("<format-list direction=\"").print(direction.name()).println("\">");
        if (registeredFormatList != null && !registeredFormatList.isEmpty()) {
            String nextIndent = indent + INDENT;
            Iterator<Entry<String, FormatEntry>> it = registeredFormatList.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, FormatEntry> entry = it.next();
                FormatEntry fe = entry.getValue();
                xml.print(nextIndent)
                .print("<format-entry name=\"").print(fe.getName()).print("\" direction=\"").print(fe.getDirection().name()).println("\">");
                writeFormatToXml(xml, fe.getFormat(), nextIndent + INDENT);
                xml.print(nextIndent)
                .println("</format-entry>");
            }
        }
        xml.print(indent)
        .println("</format-list>");
    }

    private void writeFormatToXml(StringUTF8Writer xml, Format format, String indent) throws Exception {
        FormatSerializer formatXml = new FormatSerializer(format);
        formatXml.initIndent(indent);
        formatXml.writeToXml(xml.getOutStream());
    }

    private void writeDataTransformatorListToXml(StringUTF8Writer xml, List<DataTransformator> dataTransformatorList, String indent) throws Exception {
        xml.print(indent)
        .println("<instrument-list>");
        if (dataTransformatorList != null && !dataTransformatorList.isEmpty()) {
            String nextIndent = indent + INDENT;
            for (DataTransformator dt : dataTransformatorList) {
                writeDataTransformatorToXml(xml, dt, nextIndent);
            }
        }
        xml.print(indent)
        .println("</instrument-list>");
    }

    private void writeDataTransformatorToXml(StringUTF8Writer xml, DataTransformator dt, String indent) throws Exception {
        String nextIndent = indent + INDENT;
        xml.print(indent)
        .print("<transformator type=\"").print(dt.getComponentType()).print("\" id=\"").print(dt.getId()).print("\" name=\"").print(dt.getName() == null ? "" : dt.getName()).println("\">").print(nextIndent)
        .println("<description>")
        .print("<![CDATA[").print(dt.getDescription() == null ? "" : dt.getDescription()).println("]]>").print(nextIndent)
        .println("</description>");
        xml.print(nextIndent).print("<position x=\"").print(Integer.toString(dt.getX())).print("\" y=\"").print(Integer.toString(dt.getY())).println("\"/>");
        writeTargetListToXml(xml, dt.getTargetList(), nextIndent);
        writeTransformRuleList(xml, dt.getTransformRuleList(), nextIndent);
        xml.print(indent).println("</transformator>");
    }

    private void writeStartEntryListToXml(StringUTF8Writer xml, List<StartEntry> startEntryList, String indent) throws Exception {
        if (startEntryList != null && !startEntryList.isEmpty()) {
            String nextIndent = indent + INDENT;
            xml.print(indent)
            .println("<start-entry-list>");
            for (StartEntry se : startEntryList) {
                writeStartEntryToXml(xml, se, nextIndent);
            }
            xml.print(indent)
            .println("</start-entry-list>");
        }
    }

    private void writeStartEntryToXml(StringUTF8Writer xml, StartEntry se, String indent) throws Exception {
        xml.print(indent).print("<start-entry format-name=\"").print(se.getStartFormatName()).print("\" target-id=\"").print(se.getStartComponent().getId()).println("\"/>");
    }

    private void writeTransformRuleList(StringUTF8Writer xml, List<TransformRuleEntry> transformRuleList, String indent) throws Exception {
        if (transformRuleList != null && !transformRuleList.isEmpty()) {
            String nextIndent = indent + INDENT;
            xml.print(indent)
            .println("<transform-rule-list>");
            for (TransformRuleEntry ruleEntry : transformRuleList)
                writeTransformRuleEntry(xml, ruleEntry, nextIndent);
            xml.print(indent)
            .println("</transform-rule-list>");
        }
    }

    private void writeTransformRuleEntry(StringUTF8Writer xml, TransformRuleEntry ruleEntry, String indent) throws Exception {
        xml.print(indent).print("<transform-rule-entry name=\"").print(ruleEntry.getName()).print("\"");
        xml.print(" source-id=\"").print(ruleEntry.getSourceId()).print("\"");
        xml.print(" target-id=\"").print(ruleEntry.getTargetId()).print("\"");
        xml.print(" source-format-name=\"").print(ruleEntry.getSourceFormatName()).print("\"");
        xml.print(" target-format-name=\"").print(ruleEntry.getTargetFormatName()).println("\">");
        writeRuleToXml(xml, ruleEntry.getRule(), indent + INDENT);
        xml.print(indent).println("</transform-rule-entry>");
    }

    private void writeRuleToXml(StringUTF8Writer xml, TransformRule rule, String indent) throws Exception {
        TransformRuleSerializer ruleXml = new TransformRuleSerializer(rule);
        ruleXml.initIndent(indent);
        ruleXml.writeToXml(xml.getOutStream());
    }

    //Json
    private void writeDatasourceListToJson(JsonGenerator jGenerator, List<DataSource> datasourceList) throws Exception {
        jGenerator.writeArrayFieldStart("datasource");
        if (datasourceList != null && !datasourceList.isEmpty()) {
            for (DataSource ds : datasourceList)
                writeDatasourceToJson(jGenerator, ds);
        }
        jGenerator.writeEndArray();
    }

    private void writeDatasourceToJson(JsonGenerator jGenerator, DataSource ds) throws Exception {
        jGenerator.writeStartObject();
        jGenerator.writeStringField("type", ds.getComponentType());
        jGenerator.writeStringField("id", ds.getId());
        jGenerator.writeStringField("name", ds.getName() == null ? "" : ds.getName());
        jGenerator.writeStringField("description", ds.getDescription() == null ? "" : ds.getDescription());
        jGenerator.writeObjectFieldStart("position");
        jGenerator.writeNumberField("x", ds.getX());
        jGenerator.writeNumberField("y", ds.getY());
        jGenerator.writeEndObject();
        writeDatasourceParameterListToJson(jGenerator, ds.getPropertyList());
        writeRegisterFormatListToJson(jGenerator, ds);
        writeTargetListToJson(jGenerator, ds.getTargetList());
        jGenerator.writeEndObject();
    }

    private void writeDatasourceParameterListToJson(JsonGenerator jGenerator, Map<String, Value> propertyList) throws IOException {
        jGenerator.writeArrayFieldStart("property");
        if (propertyList != null && !propertyList.isEmpty()) {
            Iterator<Entry<String, Value>> it = propertyList.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Value> entry = it.next();
                jGenerator.writeStartObject();
                jGenerator.writeStringField("name", entry.getKey());
                Value v = entry.getValue();
                jGenerator.writeStringField("type", v.type().toString());
                if (v.isByte())
                    jGenerator.writeNumberField("value", v.getByte());
                else if (v.isShort())
                    jGenerator.writeNumberField("value", v.getShort());
                else if (v.isInt())
                    jGenerator.writeNumberField("value", v.getInt());
                else if (v.isLong())
                    jGenerator.writeNumberField("value", v.getLong());
                else if (v.isFloat())
                    jGenerator.writeNumberField("value", v.getFloat());
                else if (v.isDouble())
                    jGenerator.writeNumberField("value", v.getDouble());
                else
                    jGenerator.writeStringField("value", v.toString());
                jGenerator.writeEndObject();
            }
        }
        jGenerator.writeEndArray();
    }

    private void writeRegisterFormatListToJson(JsonGenerator jGenerator, DataSource ds) throws Exception {
        Map<String, FormatEntry> registeredInFormatList = ds.getRegisteredFormatList(Direction.IN);
        Map<String, FormatEntry> registeredOutFormatList = ds.getRegisteredFormatList(Direction.OUT);
        jGenerator.writeArrayFieldStart("format_list");
        if ((registeredInFormatList != null && !registeredInFormatList.isEmpty())
            || (registeredOutFormatList != null && !registeredOutFormatList.isEmpty())) {

            if (registeredInFormatList != null && !registeredInFormatList.isEmpty()) {
                jGenerator.writeStartObject();
                jGenerator.writeStringField("direction", Direction.IN.name());
                Iterator<Entry<String, FormatEntry>> it = registeredInFormatList.entrySet().iterator();
                jGenerator.writeArrayFieldStart("format_entry");
                while (it.hasNext()) {
                    Entry<String, FormatEntry> entry = it.next();
                    FormatEntry fe = entry.getValue();
                    jGenerator.writeStartObject();
                    jGenerator.writeStringField("name", fe.getName());
                    jGenerator.writeStringField("direction", fe.getDirection().name());
                    writeFormatToJson(jGenerator, fe.getFormat());
                    jGenerator.writeEndObject();
                }
                jGenerator.writeEndArray();
                jGenerator.writeEndObject();
            }
            if (registeredOutFormatList != null && !registeredOutFormatList.isEmpty()) {
                jGenerator.writeStartObject();
                jGenerator.writeStringField("direction", Direction.OUT.name());
                Iterator<Entry<String, FormatEntry>> it = registeredOutFormatList.entrySet().iterator();
                jGenerator.writeArrayFieldStart("format_entry");
                while (it.hasNext()) {
                    Entry<String, FormatEntry> entry = it.next();
                    FormatEntry fe = entry.getValue();
                    jGenerator.writeStartObject();
                    jGenerator.writeStringField("name", fe.getName());
                    jGenerator.writeStringField("direction", fe.getDirection().name());
                    writeFormatToJson(jGenerator, fe.getFormat());
                    jGenerator.writeEndObject();
                }
                jGenerator.writeEndArray();
                jGenerator.writeEndObject();
            }
        }
        jGenerator.writeEndArray();
    }

    private void writeTargetListToJson(JsonGenerator jGenerator, Map<String, TransformComponent> targetList) throws IOException {
        jGenerator.writeArrayFieldStart("target");
        if (targetList != null) {
            Iterator<Entry<String, TransformComponent>> it = targetList.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, TransformComponent> entry = it.next();
                jGenerator.writeStartObject();
                jGenerator.writeStringField("id", entry.getKey());
                jGenerator.writeEndObject();
            }
        }
        jGenerator.writeEndArray();
    }

    private void writeFormatToJson(JsonGenerator jGenerator, Format format) throws Exception {
        FormatSerializer formatWriter = new FormatSerializer(format);
        formatWriter.writeToJson(jGenerator);
    }

    private void writeDataTransformatorListToJson(JsonGenerator jGenerator, List<DataTransformator> dataTransformatorList) throws Exception {
        jGenerator.writeArrayFieldStart("transformator");
        if (dataTransformatorList != null && !dataTransformatorList.isEmpty()) {
            for (DataTransformator dt : dataTransformatorList)
                writeDataTransformatorToJson(jGenerator, dt);
        }
        jGenerator.writeEndArray();
    }

    private void writeDataTransformatorToJson(JsonGenerator jGenerator, DataTransformator dt) throws Exception {
        jGenerator.writeStartObject();
        jGenerator.writeStringField("type", dt.getComponentType());
        jGenerator.writeStringField("id", dt.getId());
        jGenerator.writeStringField("name", dt.getName() == null ? "" : dt.getName());
        jGenerator.writeStringField("description", dt.getDescription() == null ? "" : dt.getDescription());
        jGenerator.writeObjectFieldStart("position");
        jGenerator.writeNumberField("x", dt.getX());
        jGenerator.writeNumberField("y", dt.getY());
        jGenerator.writeEndObject();
        writeTargetListToJson(jGenerator, dt.getTargetList());
        writeTransformRuleListToJson(jGenerator, dt.getTransformRuleList());
        jGenerator.writeEndObject();
    }

    private void writeStartEntryListToJson(JsonGenerator jGenerator, List<StartEntry> startEntryList) throws Exception {
        if (startEntryList != null && !startEntryList.isEmpty()) {
            jGenerator.writeArrayFieldStart("start_entry");
            for (StartEntry se : startEntryList) {
                writeStartEntryToJson(jGenerator, se);
            }
            jGenerator.writeEndArray();
        }
    }

    private void writeStartEntryToJson(JsonGenerator jGenerator, StartEntry se) throws Exception {
        jGenerator.writeStartObject();
        jGenerator.writeStringField("format_name", se.getStartFormatName());
        jGenerator.writeStringField("target_name", se.getStartComponent().getName());
        jGenerator.writeEndObject();
    }

    private void writeTransformRuleListToJson(JsonGenerator jGenerator, List<TransformRuleEntry> transformRuleList) throws Exception {
        if (transformRuleList != null && !transformRuleList.isEmpty()) {
            jGenerator.writeArrayFieldStart("transform_rule_entry");
            for (TransformRuleEntry ruleEntry : transformRuleList)
                writeTransformRuleEntryToJson(jGenerator, ruleEntry);
            jGenerator.writeEndArray();
        }
    }

    private void writeTransformRuleEntryToJson(JsonGenerator jGenerator, TransformRuleEntry ruleEntry) throws Exception {
        jGenerator.writeStartObject();
        jGenerator.writeStringField("name", ruleEntry.getName());
        jGenerator.writeStringField("source_id", ruleEntry.getSourceId());
        jGenerator.writeStringField("target_id", ruleEntry.getTargetId());
        jGenerator.writeStringField("source_format_name", ruleEntry.getSourceFormatName());
        jGenerator.writeStringField("target_format_name", ruleEntry.getTargetFormatName());
        writeRuleToJson(jGenerator, ruleEntry.getRule());
        jGenerator.writeEndObject();
    }

    private void writeRuleToJson(JsonGenerator jGenerator, TransformRule rule) throws Exception {
        TransformRuleSerializer ruleWriter = new TransformRuleSerializer(rule);
        ruleWriter.writeToJson(jGenerator);
    }
}
