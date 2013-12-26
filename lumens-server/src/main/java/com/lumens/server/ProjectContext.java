/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.server;

import com.lumens.engine.TransformProject;
import java.util.HashMap;
import java.util.Map;

/**
 * Hold all the project instances information
 */
public class ProjectContext {

    private Map<ProjectIndex, TransformProject> projecInstanceList = new HashMap<>();

    public ProjectContext add(TransformProject project) {
        projecInstanceList.put(new ProjectIndex(project.getName(), project.getDescription()), project);
        return this;
    }

    public ProjectContext remove(TransformProject project) {
        projecInstanceList.remove(new ProjectIndex(project.getName(), project.getDescription()));
        return this;
    }

    public TransformProject lookupProject(ProjectIndex projectName) {
        return projecInstanceList.get(projectName);
    }
}
