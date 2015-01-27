/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.dao;

import com.lumens.sysdb.entity.Project;
import java.util.List;

public class ProjectDAO extends BaseDAO {

    public int getTotal() {
        return super.getTotal("ProjectDAO/total");
    }

    public long create(final Project project) {
        this.simpleTransactionExecute(sqlManager.getSQL("ProjectDAO/CreateProject", project.name, project.description, project.data));
        return project.id;
    }

    public long update(final Project project) {
        this.simpleTransactionExecute(sqlManager.getSQL("ProjectDAO/UpdateProject", project.name, project.description, project.data, project.id));
        return project.id;
    }

    public long delete(final long projectId) {
        this.simpleTransactionExecute(sqlManager.getSQL("ProjectDAO/DeleteProject", projectId));
        return projectId;
    }

    public List<Project> getAllShortProject() {
        return simpleQuery(sqlManager.getSQL("ProjectDAO/AllShortProject"), Project.class);
    }

    public Project getProject(long projectId) {
        List<Project> pList = simpleQuery(sqlManager.getSQL("ProjectDAO/FindProject", projectId), Project.class);
        return pList.size() > 0 ? pList.get(0) : null;
    }

    public List<Project> getAllProject() {
        return simpleQuery(sqlManager.getSQL("ProjectDAO/AllProject"), Project.class);
    }
}
