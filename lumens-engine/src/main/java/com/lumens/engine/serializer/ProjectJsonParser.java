/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.serializer;

import com.lumens.engine.TransformProject;
import java.io.InputStream;
import java.util.Iterator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * It is used to parse JSON of project
 */
public class ProjectJsonParser {

    public static final String PROJECT = "project";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STARTENTRY_LIST = "start-entry-list";
    public static final String DATASOURCE_LIST = "datasource-list";
    public static final String PROCESSOR_LIST = "processor-list";
    private TransformProject project;

    public ProjectJsonParser(TransformProject project) {
        this.project = project;
    }

    public void parse(InputStream in) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode json = om.readTree(in);
        JsonNode projectJson = json.get(PROJECT);
        readProjectFromJson(projectJson);
    }

    private void readProjectFromJson(JsonNode projectJson) {
        // Read project properties
        JsonNode nameJson = projectJson.get(NAME);
        JsonNode descJson = projectJson.get(DESCRIPTION);
        // Read list
        readProjectStartEntryFromJson(projectJson.get(STARTENTRY_LIST));
        readProjectDataSourceListFromJson(projectJson.get(DATASOURCE_LIST));
        readProjectProcessorListFromJson(projectJson.get(PROCESSOR_LIST));
    }

    private void readProjectDataSourceListFromJson(JsonNode dsJson) {
        if (dsJson.isArray()) {
            Iterator<JsonNode> it = dsJson.getElements();
            while (it.hasNext())
                readDataSourceFromJson(it.next());
        } else if (dsJson.isObject()) {
            readDataSourceFromJson(dsJson);
        }
    }

    private void readProjectProcessorListFromJson(JsonNode processorJson) {
        if (processorJson.isArray()) {
            Iterator<JsonNode> it = processorJson.getElements();
            while (it.hasNext())
                readProcessorFromJson(it.next());
        } else if (processorJson.isObject()) {
            readProcessorFromJson(processorJson);
        }
    }

    private void readProjectStartEntryFromJson(JsonNode startJson) {
        if (startJson.isArray()) {
            Iterator<JsonNode> it = startJson.getElements();
            while (it.hasNext())
                readStartEntryFromJson(it.next());
        } else if (startJson.isObject()) {
            readStartEntryFromJson(startJson);
        }
    }

    private void readDataSourceFromJson(JsonNode next) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void readProcessorFromJson(JsonNode next) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void readStartEntryFromJson(JsonNode next) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
