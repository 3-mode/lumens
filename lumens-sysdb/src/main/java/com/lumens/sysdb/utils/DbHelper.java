/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.utils;

import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.JobProjectRelationDAO;
import com.lumens.sysdb.dao.ProjectDAO;
import com.lumens.sysdb.entity.JobProjectRelation;
import com.lumens.sysdb.entity.Project;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DbHelper {
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
    
    public static List<String> loadProjectIdFromDb(long jobId) {
        List<String> projectIdList = new ArrayList();
        JobProjectRelationDAO relationDAO = DAOFactory.getRelationDAO();        
        List<JobProjectRelation> relationList = relationDAO.getAllRelation(jobId);
        for (JobProjectRelation relation : relationList) {
            projectIdList.add(String.valueOf(relation.projectId));
        }

        return projectIdList;
    }    
}
