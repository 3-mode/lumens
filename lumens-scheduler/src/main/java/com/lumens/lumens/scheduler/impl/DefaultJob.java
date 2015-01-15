/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.lumens.scheduler.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.lumens.engine.TransformProject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.Date;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DefaultJob implements Job {

    private long jobId;
    private String name;
    private String description;
    List<TransformProject> projectList = new ArrayList();
    Map<String, TransformProject> projectMap = new HashMap<>();

    public DefaultJob(long jobId, String name, String description) {
        this.jobId = jobId;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return jobId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public DefaultJob addProject(TransformProject project){
        String projectName = project.getName();
        if (projectMap.containsKey(projectName)){
            projectMap.put(projectName, project);
            projectList.add(project);
        }
        
        return this;
    }
    
    public List<TransformProject> getProjectList(){
        return projectList;
    }

    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("[" + (new Date()) + "]" + jec.getTrigger().getKey().getName() + " job is running");
    }
}
