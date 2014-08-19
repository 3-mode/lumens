/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.engine.TransformProject;
import java.util.HashMap;
import java.util.Map;

/**
 * Hold all the project instances information
 */
public class ProjectContext {

    private Map<ProjectIndex, TransformProject> projecInstanceList = new HashMap<>();

    public ProjectContext add(String projectID, TransformProject project) {
        projecInstanceList.put(new ProjectIndex(projectID, project.getDescription()), project);
        return this;
    }

    public ProjectContext remove(String projectID) {
        projecInstanceList.remove(new ProjectIndex(projectID, null));
        return this;
    }

    public TransformProject lookupProject(ProjectIndex projectName) {
        return projecInstanceList.get(projectName);
    }
}
