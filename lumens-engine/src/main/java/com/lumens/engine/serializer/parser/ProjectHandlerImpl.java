/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.serializer.parser;

import com.lumens.connector.Direction;
import com.lumens.engine.StartEntry;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.component.instrument.DataTransformator;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.RegisterFormatComponent;
import com.lumens.engine.component.RuleComponent;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import com.lumens.model.serializer.parser.FormatHandler;
import com.lumens.model.serializer.parser.FormatHandlerImpl;
import com.lumens.processor.transform.TransformRule;
import com.lumens.processor.transform.serializer.parser.TransformRuleHandler;
import com.lumens.processor.transform.serializer.parser.TransformRuleHandlerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class ProjectHandlerImpl implements ProjectHandler {

    public enum ReadStatus {

        NONE,
        PROJECT,
        DATASRC,
        DATAPSR,
        FORMAT
    }
    // Project
    private ReadStatus status = ReadStatus.NONE;
    private TransformProject project;
    private Map<String, TransformComponent> tComponentCache = new HashMap<>();
    private Map<String, List<String>> targetList = new HashMap<>();
    private TransformComponent tc;
    // Format
    private FormatHandler formatHandler;
    private Map<String, Value> propList;
    private Map<String, FormatEntry> registeredFormatList;
    private FormatEntry formatEntry;
    private List<Format> formatList;
    // Rule
    private TransformRuleHandler transformRuleHandler;
    private TransformRuleEntry curRuleEntry;
    private String curComponentTargetName;
    private List<TransformRule> ruleList;
    // Start
    private List<StartEntry> startList;

    public ProjectHandlerImpl(TransformProject project) {
        this.project = project;
    }

    @Override
    public void start_project(final Attributes meta) throws SAXException {
        status = ReadStatus.PROJECT;
        String name = meta.getValue("name");
        if (name == null)
            throw new SAXException("Project name can not be empty !");
        project.setName(name);
    }

    @Override
    public void end_project() throws SAXException {
        Iterator<Entry<String, List<String>>> it = targetList.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, List<String>> entry = it.next();
            TransformComponent s = tComponentCache.get(entry.getKey());
            for (String targetName : entry.getValue()) {
                TransformComponent t = tComponentCache.get(targetName);
                if (t != null)
                    s.targetTo(t);
            }
        }
        status = ReadStatus.NONE;
    }

    @Override
    public void handle_description(final String data,
                                   final Attributes meta) throws SAXException {
        if (status == ReadStatus.PROJECT && data != null)
            project.setDescription(data.trim());
        else if (data != null)
            tc.setDescription(data.trim());
    }

    @Override
    public void handle_position(final Attributes meta) throws SAXException {
        if (tc != null) {
            tc.setX(Integer.parseInt(meta.getValue("x")));
            tc.setY(Integer.parseInt(meta.getValue("y")));
        }
    }

    @Override
    public void start_resource_list(final Attributes meta) throws SAXException {
    }

    @Override
    public void end_resource_list() throws SAXException {
    }

    @Override
    public void start_datasource(final Attributes meta) throws SAXException {
        if (status == ReadStatus.PROJECT) {
            status = ReadStatus.DATASRC;
            String id = meta.getValue("id");
            if (id == null || id.isEmpty())
                throw new SAXException("Identifier can not be empty !");
            tc = new DataSource(id);
            String name = meta.getValue("name");
            if (name == null || name.isEmpty())
                throw new SAXException("Data source name can not be empty !");
            tc.setName(name);
            tComponentCache.put(tc.getName(), tc);
        } else
            throw new SAXException("Error status \'" + status + "\' to read datasource list!");
    }

    @Override
    public void end_datasource() throws SAXException {
        if (status == ReadStatus.DATASRC)
            project.getDatasourceList().add((DataSource) tc);
        else
            throw new SAXException("Error status \'" + status + "\' to read datasource list!");
        tc = null;
        status = ReadStatus.PROJECT;
    }

    @Override
    public void start_format_list(final Attributes meta) throws SAXException {
        if (status == ReadStatus.DATASRC) {
            registeredFormatList = ((DataSource) tc).getRegisteredFormatList(
            Direction.valueOf(meta.getValue("direction")));
        }
    }

    @Override
    public void end_format_list() throws SAXException {
        registeredFormatList = null;
    }

    @Override
    public void start_format_entry(final Attributes meta) throws SAXException {
        if (status == ReadStatus.DATASRC && registeredFormatList != null) {
            formatEntry = new FormatEntry(meta.getValue("name"), null,
                                          Direction.valueOf(meta.getValue("direction")));
            registeredFormatList.put(formatEntry.getName(), formatEntry);
            if (formatList == null) {
                formatList = new ArrayList<>();
                formatHandler = new FormatHandlerImpl(formatList);
            }
            status = ReadStatus.FORMAT;
        }
    }

    @Override
    public void end_format_entry() throws SAXException {
        if (status == ReadStatus.FORMAT && formatEntry != null
            && formatList != null && !formatList.isEmpty()) {
            formatEntry.setFormat(formatList.get(0));
        }

        formatEntry = null;
        formatList.clear();
        status = ReadStatus.DATASRC;
    }

    @Override
    public void start_format(final Attributes meta) throws SAXException {
        if (status == ReadStatus.FORMAT && formatHandler != null)
            formatHandler.start_format(meta);
    }

    @Override
    public void end_format() throws SAXException {
        if (status == ReadStatus.FORMAT && formatHandler != null)
            formatHandler.end_format();
    }

    @Override
    public void start_property_list(final Attributes meta) throws SAXException {
        if (status == ReadStatus.DATASRC) {
            propList = new HashMap<>();
        }
    }

    @Override
    public void end_property_list() throws SAXException {
        if (tc != null && status == ReadStatus.DATASRC) {
            DataSource ds = (DataSource) tc;
            ds.setPropertyList(propList);
        }
        propList = null;
    }

    @Override
    public void handle_property(final String data, final Attributes meta) throws SAXException {
        if (status == ReadStatus.DATASRC && propList != null) {
            propList.put(meta.getValue("name"), new Value(Type.parseString(meta.getValue("type")), data));
        } else if (status == ReadStatus.FORMAT && formatHandler != null) {
            formatHandler.handle_property(data, meta);
        }
    }

    @Override
    public void start_target_list(final Attributes meta) throws SAXException {
    }

    @Override
    public void end_target_list() throws SAXException {
    }

    @Override
    public void handle_target(final Attributes meta) throws SAXException {
        if (tc != null) {
            String srcName = tc.getName();
            List<String> currentTargetList = this.targetList.get(srcName);
            if (currentTargetList == null) {
                currentTargetList = new ArrayList<>();
                this.targetList.put(srcName, currentTargetList);
            }
            curComponentTargetName = meta.getValue("name");
            currentTargetList.add(curComponentTargetName);
        }
    }

    @Override
    public void start_instrument_list(final Attributes meta) throws SAXException {
    }

    @Override
    public void end_instrument_list() throws SAXException {
    }

    @Override
    public void start_transformator(final Attributes meta) throws SAXException {
        if (status == ReadStatus.PROJECT) {
            status = ReadStatus.DATAPSR;
            String id = meta.getValue("id");
            tc = new DataTransformator();
            if (id == null || id.isEmpty() || !tc.getIdentifier().equals(id))
                throw new SAXException("Error, the property 'id' is empty !");
            tc = new DataTransformator();
            tc.setName(meta.getValue("name"));
            tComponentCache.put(tc.getName(), tc);
        } else
            throw new SAXException("Error status \'" + status + "\' to read processor list!");
    }

    @Override
    public void end_transformator() throws SAXException {
        if (status == ReadStatus.DATAPSR)
            project.getDataTransformatorList().add((DataTransformator) tc);
        status = ReadStatus.PROJECT;
    }

    @Override
    public void start_transform_rule_list(final Attributes meta) throws SAXException {
    }

    @Override
    public void end_transform_rule_list() throws SAXException {
    }

    @Override
    public void start_transform_rule_entry(Attributes meta) throws SAXException {
        if (status == ReadStatus.DATAPSR) {
            curRuleEntry = new TransformRuleEntry(meta.getValue("name"),
                                                  meta.getValue("source-name"),
                                                  meta.getValue("target-name"));
        }
    }

    @Override
    public void end_transform_rule_entry() throws SAXException {
        if (status == ReadStatus.DATAPSR) {
            if (tc instanceof RuleComponent && ruleList != null && !ruleList.isEmpty()) {
                curRuleEntry.setRule(ruleList.get(0));
                ((RuleComponent) tc).registerRule(curRuleEntry);
            }
        }
    }

    @Override
    public void start_transform_rule(final Attributes meta) throws SAXException {
        if (status == ReadStatus.DATAPSR) {
            if (tc.isSingleTarget() && tc instanceof RuleComponent) {
                TransformComponent tComp = tComponentCache.get(curComponentTargetName);
                if (tComp instanceof RegisterFormatComponent) {
                    RegisterFormatComponent rfComp = (RegisterFormatComponent) tComp;
                    FormatEntry fEntry = rfComp.getRegisteredFormatList(Direction.IN).get(curRuleEntry.getTargetName());
                    Format format = fEntry.getFormat();
                    ruleList = new ArrayList<>();
                    transformRuleHandler = new TransformRuleHandlerImpl(format, ruleList);
                    transformRuleHandler.start_transform_rule(meta);
                }
            } else
                throw new SAXException("Error, the current component is not a rule component !");
        }
    }

    @Override
    public void end_transform_rule() throws SAXException {
        if (status == ReadStatus.DATAPSR) {
            if (transformRuleHandler != null)
                transformRuleHandler.end_transform_rule();
        }
    }

    @Override
    public void start_transform_rule_item(final Attributes meta) throws SAXException {
        if (status == ReadStatus.DATAPSR) {
            if (transformRuleHandler != null)
                transformRuleHandler.start_transform_rule_item(meta);
        }
    }

    @Override
    public void end_transform_rule_item() throws SAXException {
        if (status == ReadStatus.DATAPSR) {
            if (transformRuleHandler != null)
                transformRuleHandler.end_transform_rule_item();
        }
    }

    @Override
    public void handle_script(final String data,
                              final Attributes meta) throws SAXException {
        if (status == ReadStatus.DATAPSR) {
            if (transformRuleHandler != null)
                transformRuleHandler.handle_script(data, meta);
        }
    }

    @Override
    public void start_start_entry_list(Attributes meta) throws SAXException {
        startList = new ArrayList<>();
    }

    @Override
    public void end_start_entry_list() throws SAXException {
        project.getStartEntryList().addAll(startList);
    }

    @Override
    public void handle_start_entry(Attributes meta) throws SAXException {
        startList.add(new StartEntry(meta.getValue("name"), tComponentCache.get(meta.getValue("entry-name"))));
    }
}