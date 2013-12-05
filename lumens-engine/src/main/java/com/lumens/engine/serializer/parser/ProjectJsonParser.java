/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.serializer.parser;

import com.lumens.engine.TransformProject;
import java.io.InputStream;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * It is used to parse JSON of project
 */
public class ProjectJsonParser {

    private TransformProject project;

    public ProjectJsonParser(TransformProject project) {
        this.project = project;
    }

    public void parse(InputStream in) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode json = om.readTree(in);
    }
}
