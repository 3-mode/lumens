/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.TransformProject;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.JobProjectRelationDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.entity.JobProjectRelation;
import com.lumens.sysdb.entity.Project;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DBHelper {
    public static List<Project> loadProjectFromDb(long jobId) {
        List<Project> projectList = new ArrayList();
        JobProjectRelationDAO relationDAO = DAOFactory.getRelationDAO();
        ProjectDAO projectDAO = DAOFactory.getProjectDAO();
        List<JobProjectRelation> relationList = relationDAO.getAllRelation(jobId);
        for (JobProjectRelation relation : relationList) {
            projectList.add(projectDAO.getProject(relation.projectId));
        }

        return projectList;
    }

    public static List<Project> loadShortProjectFromDb(long jobId) {
        List<Project> projectList = new ArrayList();
        JobProjectRelationDAO relationDAO = DAOFactory.getRelationDAO();
        ProjectDAO projectDAO = DAOFactory.getProjectDAO();
        List<JobProjectRelation> relationList = relationDAO.getAllRelation(jobId);
        for (JobProjectRelation relation : relationList) {
            projectList.add(projectDAO.getShortProjectByID(relation.projectId));
        }

        return projectList;
    }

    public static List<String> loadProjectIdFromDb(long jobId) {
        List<String> projectIdList = new ArrayList();
        JobProjectRelationDAO relationDAO = DAOFactory.getRelationDAO();
        List<JobProjectRelation> relationList = relationDAO.getAllRelation(jobId);
        for (JobProjectRelation relation : relationList) {
            projectIdList.add(Long.toString(relation.projectId));
        }

        return projectIdList;
    }

    public static List<TransformProject> loadTransformProjectFromDb(long jobId) throws Exception {
        List<TransformProject> transformProjectList = new ArrayList<>();
        List<Project> projectList = loadProjectFromDb(jobId);
        for (Project project : projectList) {
            TransformProject projectInst = new TransformProject();
            projectInst.setID(project.id);
            new ProjectSerializer(projectInst).readFromJson(new ByteArrayInputStream(project.data.getBytes()));
            transformProjectList.add(projectInst);
        }
        return transformProjectList;
    }
}
