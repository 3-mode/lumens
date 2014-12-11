/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.serializer;

import com.lumens.connector.Direction;
import com.lumens.engine.StartEntry;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.component.instrument.DataTransformer;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.RegisterFormatComponent;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import com.lumens.model.serializer.FormatSerializer;
import com.lumens.processor.transform.TransformForeach;
import com.lumens.processor.transform.TransformRule;
import com.lumens.processor.transform.TransformRuleItem;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.NullNode;

/**
 *
 * It is used to parse JSON of project
 */
public class ProjectJsonParser {

    public static final String PROJECT = "project";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STARTENTRY_LIST = "start_entry";
    public static final String DATASOURCE_LIST = "datasource";
    public static final String TRANSFORMER_LIST = "transformer";
    private final TransformProject project;
    private final Map<String, TransformComponent> tComponentCache = new HashMap<>();
    private final Map<String, List<String>> targetList = new HashMap<>();

    public ProjectJsonParser(TransformProject project) {
        this.project = project;
    }

    public void parse(InputStream in) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode json = om.readTree(in);
        readProjectFromJson(json.get(PROJECT));
    }

    public void parse(JsonNode projectRootJson) throws Exception {
        readProjectFromJson(projectRootJson.get(PROJECT));
    }

    private boolean isNotNull(JsonNode json) {
        return json != null && json != NullNode.instance;
    }

    private void readProjectFromJson(JsonNode projectJson) {
        if (isNotNull(projectJson)) {
            // Read project properties
            JsonNode nameJson = projectJson.get(NAME);
            JsonNode descJson = projectJson.get(DESCRIPTION);
            if (isNotNull(nameJson)) {
                project.setName(nameJson.asText());
                project.setDescription(descJson.asText());
                // Read list
                readProjectDataSourceListFromJson(projectJson.get(DATASOURCE_LIST));
                readProjectInstrumentListFromJson(projectJson.get(TRANSFORMER_LIST));
                readProjectStartEntryFromJson(projectJson.get(STARTENTRY_LIST));
                handleTargetList();
            } else
                throw new RuntimeException("Invalid project no name !");
        }
    }

    private void readProjectDataSourceListFromJson(JsonNode dsArrayJson) {
        if (isNotNull(dsArrayJson) && dsArrayJson.isArray()) {
            Iterator<JsonNode> it = dsArrayJson.getElements();
            while (it.hasNext())
                readDataSourceFromJson(it.next());
        }
    }

    private void readProjectInstrumentListFromJson(JsonNode transformerJson) {
        if (isNotNull(transformerJson) && transformerJson.isArray()) {
            Iterator<JsonNode> it = transformerJson.getElements();
            while (it.hasNext())
                readProcessorFromJson(it.next());
        }
    }

    private void readProjectStartEntryFromJson(JsonNode startJson) {
        if (isNotNull(startJson) && startJson.isArray()) {
            Iterator<JsonNode> it = startJson.getElements();
            while (it.hasNext())
                readStartEntryFromJson(it.next());
        }
    }

    private void readDataSourceFromJson(JsonNode datasourceJson) {
        if (isNotNull(datasourceJson)) {
            JsonNode idJson = datasourceJson.get("id");
            JsonNode typeJson = datasourceJson.get("type");
            JsonNode nameJson = datasourceJson.get("name");
            JsonNode dscJson = datasourceJson.get("description");
            JsonNode posJson = datasourceJson.get("position");
            if (isNotNull(typeJson) && isNotNull(idJson) && isNotNull(nameJson)) {
                DataSource ds = new DataSource(typeJson.asText(), idJson.asText());
                ds.setName(nameJson.asText());
                if (isNotNull(dscJson))
                    ds.setDescription(dscJson.asText());
                if (isNotNull(posJson)) {
                    ds.setX(posJson.get("x").asInt());
                    ds.setY(posJson.get("y").asInt());
                }
                project.getDatasourceList().add(ds);
                tComponentCache.put(ds.getId(), ds);
                readDataSourceProperties(ds, datasourceJson);
                readDataSourceFormatList(ds, datasourceJson);
                readTransformComponentTargetList(ds, datasourceJson);
            } else
                throw new RuntimeException("Data source name or class name is invalid !");
        }
    }

    private void readProcessorFromJson(JsonNode transformerJson) {
        if (isNotNull(transformerJson)) {
            JsonNode idJson = transformerJson.get("id");
            JsonNode typeJson = transformerJson.get("type");
            JsonNode nameJson = transformerJson.get("name");
            JsonNode dscJson = transformerJson.get("description");
            JsonNode posJson = transformerJson.get("position");
            if (isNotNull(typeJson) && isNotNull(idJson) && isNotNull(nameJson)) {
                DataTransformer dt = new DataTransformer(idJson.asText());
                if (dt.getComponentType().equals(typeJson.asText())) {
                    dt.setName(nameJson.asText());
                    if (isNotNull(dscJson))
                        dt.setDescription(dscJson.asText());
                    if (isNotNull(posJson)) {
                        dt.setX(posJson.get("x").asInt());
                        dt.setY(posJson.get("y").asInt());
                    }
                    project.getDataTransformerList().add(dt);
                    tComponentCache.put(dt.getId(), dt);
                    readTransformComponentTargetList(dt, transformerJson);
                    readTransformRuleEntry(dt, transformerJson);
                } else
                    throw new RuntimeException("Processor name or class name is invalid !");
            }
        }
    }

    private void readStartEntryFromJson(JsonNode startEntryJson) {
        JsonNode nameJson = startEntryJson.get("format_name");
        JsonNode targetId = startEntryJson.get("target_id");
        if (isNotNull(nameJson) && isNotNull(targetId)) {
            TransformComponent tComponent = tComponentCache.get(targetId.asText());
            if (tComponent != null)
                project.getStartEntryList().add(new StartEntry(nameJson.asText(), tComponent));
            else
                throw new RuntimeException("Invalid target component id '" + targetId.asText() + "' !");
        }
    }

    private void handleTargetList() {
        Iterator<Entry<String, List<String>>> it = targetList.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, List<String>> entry = it.next();
            TransformComponent s = tComponentCache.get(entry.getKey());
            for (String targetId : entry.getValue()) {
                TransformComponent t = tComponentCache.get(targetId);
                if (t != null)
                    s.targetTo(t);
            }
        }
    }

    private void readDataSourceProperties(DataSource ds, JsonNode datasourceJson) {
        JsonNode propertiesJson = datasourceJson.get("property");
        if (isNotNull(propertiesJson) && propertiesJson.isArray()) {
            Iterator<JsonNode> it = propertiesJson.getElements();
            while (it.hasNext()) {
                JsonNode prop = it.next();
                JsonNode nameJson = prop.get("name");
                JsonNode typeJson = prop.get("type");
                JsonNode valueJson = prop.get("value");
                if (isNotNull(valueJson) && isNotNull(nameJson) && isNotNull(typeJson)) {
                    ds.getPropertyList().put(nameJson.asText(), new Value(Type.parseString(typeJson.asText()), valueJson.asText()));
                }
            }
        }
    }

    private void readDataSourceFormatList(DataSource ds, JsonNode datasourceJson) {
        JsonNode formatListJson = datasourceJson.get("format_list");
        if (isNotNull(formatListJson) && formatListJson.isArray()) {
            Iterator<JsonNode> it = formatListJson.getElements();
            while (it.hasNext()) {
                JsonNode formatListItemJson = it.next();
                JsonNode formatEntryListJson = formatListItemJson.get("format_entry");
                if (isNotNull(formatEntryListJson) && formatEntryListJson.isArray()) {
                    Iterator<JsonNode> entryIt = formatEntryListJson.getElements();
                    while (entryIt.hasNext()) {
                        JsonNode formatEntryJson = entryIt.next();
                        JsonNode nameJson = formatEntryJson.get("name");
                        JsonNode directJson = formatEntryJson.get("direction");
                        if (isNotNull(nameJson) && isNotNull(directJson)) {
                            Direction direct = Direction.valueOf(directJson.asText());
                            Format format = readFormatFromForEntry(ds, formatEntryJson);
                            if (format != null)
                                ds.registerFormat(nameJson.asText(), format, direct);
                        }
                    }
                }
            }
        }
    }

    private void readTransformComponentTargetList(TransformComponent tc, JsonNode targetJson) {
        JsonNode curentTargetListJson = targetJson.get("target");
        if (isNotNull(curentTargetListJson) && curentTargetListJson.isArray()) {
            Iterator<JsonNode> it = curentTargetListJson.getElements();
            while (it.hasNext()) {
                JsonNode target = it.next();
                List<String> currentTargetList = targetList.get(tc.getId());
                if (currentTargetList == null) {
                    currentTargetList = new ArrayList<>();
                    targetList.put(tc.getId(), currentTargetList);
                }
                JsonNode idJson = target.get("id");
                if (isNotNull(idJson))
                    currentTargetList.add(idJson.asText());
                else
                    throw new RuntimeException("Invalid target !");
            }
        }
    }

    private Format readFormatFromForEntry(DataSource ds, JsonNode formatEntryJson) {
        try {
            Format format = new DataFormat("Root");
            JsonNode formatJson = formatEntryJson.get("format");
            if (isNotNull(formatJson))
                new FormatSerializer(format).readFromJson(formatJson);
            Format newFormat = format.getChildren() != null && format.getChildren().size() > 0 ? format.getChildren().get(0) : null;
            if (newFormat != null)
                newFormat.setParent(null);
            return newFormat;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void readTransformRuleEntry(DataTransformer dt, JsonNode transformerJson) {
        JsonNode transformRuleEntryListJson = transformerJson.get("transform_rule_entry");
        if (isNotNull(transformRuleEntryListJson) && transformRuleEntryListJson.isArray()) {
            Iterator<JsonNode> it = transformRuleEntryListJson.getElements();
            while (it.hasNext()) {
                JsonNode transformRuleEntryJson = it.next();
                JsonNode nameJson = transformRuleEntryJson.get("name");
                JsonNode srcJson = transformRuleEntryJson.get("source_id");
                JsonNode tgtJson = transformRuleEntryJson.get("target_id");
                JsonNode srcFmtJson = transformRuleEntryJson.get("source_format_name");
                JsonNode tgtFmtJson = transformRuleEntryJson.get("target_format_name");
                if (isNotNull(nameJson) && isNotNull(srcJson) && isNotNull(tgtJson) && isNotNull(srcFmtJson) && isNotNull(tgtFmtJson)) {
                    JsonNode transformRuleJson = transformRuleEntryJson.get("transform_rule");
                    TransformRule tr = readTransformRuleFromJson(dt, tgtJson.asText(), tgtFmtJson.asText(), transformRuleJson);
                    if (tr != null) {
                        TransformRuleEntry transformRuleEntry = new TransformRuleEntry(nameJson.asText(), srcJson.asText(), srcFmtJson.asText(), tgtJson.asText(), tgtFmtJson.asText(), tr);
                        dt.registerRule(transformRuleEntry);
                    }
                }
            }
        }
    }

    private TransformRule readTransformRuleFromJson(DataTransformer dt, String targetId, String targetFmtName, JsonNode transformRuleJson) {
        if (isNotNull(transformRuleJson)) {
            // get Root transform rule item
            TransformComponent tComp = tComponentCache.get(targetId);
            if (tComp instanceof RegisterFormatComponent) {
                RegisterFormatComponent rc = (RegisterFormatComponent) tComp;
                FormatEntry fEntry = rc.getRegisteredFormatList(Direction.IN).get(targetFmtName);
                if (fEntry != null) {
                    Format format = fEntry.getFormat();
                    TransformRule rule = new TransformRule(format);
                    readTransformRuleItemFromJson(rule, transformRuleJson.get("transform_rule_item"), format.getName());
                    return rule;
                }
            }
        }
        return null;
    }

    private void readTransformRuleItemFromJson(TransformRule rule, JsonNode rootTransformRuleItemJson, String pathToken) {
        JsonNode transformRuleItemJson = rootTransformRuleItemJson.get("transform_rule_item");
        if (isNotNull(transformRuleItemJson)) {
            Iterator<JsonNode> it = transformRuleItemJson.getElements();
            while (it.hasNext()) {
                JsonNode ti = it.next();
                JsonNode fnameJson = ti.get("format_name");
                if (isNotNull(fnameJson)) {
                    String formatName = fnameJson.asText();
                    if (pathToken != null)
                        formatName = pathToken + '.' + formatName;
                    TransformRuleItem ritem = rule.getRuleItem(formatName);
                    JsonNode scriptJson = ti.get("script");
                    if (isNotNull(scriptJson)) {
                        try {
                            ritem.setScript(scriptJson.asText());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    JsonNode foreachArray = ti.get("for_each");
                    if (isNotNull(foreachArray) && foreachArray.isArray()) {
                        readTransformForeachList(ritem, foreachArray);
                    }
                    readTransformRuleItemFromJson(rule, ti, formatName);
                }
            }
        }
    }

    private void readTransformForeachList(TransformRuleItem ritem, JsonNode foreachArray) {
        Iterator<JsonNode> foreachIt = foreachArray.getElements();
        while (foreachIt.hasNext()) {
            JsonNode foreachNode = foreachIt.next();
            JsonNode value = foreachNode.get("source_path");
            String sourcePath = isNotNull(value) ? value.asText().trim() : "";
            value = foreachNode.get("short_source_path");
            String shortSourcePath = isNotNull(value) ? value.asText().trim() : "";
            value = foreachNode.get("index_name");
            String indexName = isNotNull(value) ? value.asText().trim() : "";
            value = foreachNode.get("index_value");
            String indexValue = isNotNull(value) ? value.asText().trim() : "";
            ritem.addTransformForeach(new TransformForeach(sourcePath, shortSourcePath, indexName, Integer.parseInt(indexValue)));
        }
    }
}
