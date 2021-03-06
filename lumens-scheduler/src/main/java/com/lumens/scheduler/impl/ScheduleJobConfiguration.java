/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.scheduler.impl;

import com.lumens.engine.TransformProject;
import com.lumens.engine.handler.InspectionHandler;
import com.lumens.engine.handler.ProjectInspectionHandler;
import com.lumens.engine.log.FileJobLogHandler;
import com.lumens.logsys.JobLogFactory;
import java.util.ArrayList;
import java.util.List;
import com.lumens.scheduler.JobConfiguration;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class ScheduleJobConfiguration implements JobConfiguration {
    private final long jobId;
    private final String name;
    private final String description;
    private final long startTime;
    private final long endTime;
    private final int repeat;
    private final int interval;
    private final List<TransformProject> projectList = new ArrayList();
    private List<InspectionHandler> inspectionHandlers = new ArrayList();
    private JobLogFactory jobLogFactory;
    private Logger log;

    public ScheduleJobConfiguration(JobLogFactory jobLogFactory, long jobId, String name, String description, long startTime, long endTime, int repeat, int interval) {
        this.jobLogFactory = jobLogFactory;
        this.jobId = jobId;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeat = repeat;
        this.interval = interval;
        if (jobLogFactory != null) {
            this.log = jobLogFactory.getLogger();
            inspectionHandlers.add(new FileJobLogHandler(this.log));
        }
    }

    public ScheduleJobConfiguration() {
        this.jobId = 0;
        this.name = "default";
        this.description = "";
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
        this.repeat = -1;
        this.interval = -1;
    }

    @Override
    public Long getId() {
        return jobId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRepeat() {
        return repeat;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    // In millisecond
    @Override
    public int getInterval() {
        return interval;
    }

    @Override
    public JobConfiguration addProject(TransformProject project) {
        projectList.add(project);
        return this;
    }

    @Override
    public List<TransformProject> getProjectList() {
        return projectList;
    }

    @Override
    public JobConfiguration addProject(List<TransformProject> projects) {
        projectList.addAll(projects);
        return this;
    }

    @Override
    public void setInspectionHandlers(List<InspectionHandler> handlers) {
        this.inspectionHandlers = handlers;
    }

    @Override
    public List<InspectionHandler> getInspectionHandlers() {
        return this.inspectionHandlers;
    }

    @Override
    public List<InspectionHandler> getInspectionHandlers(TransformProject project) {
        // Attach the handlers to current executable project
        for (InspectionHandler handler : getInspectionHandlers())
            if (handler instanceof ProjectInspectionHandler)
                ((ProjectInspectionHandler) handler).withProjectID(project.getID()).withProjectName(project.getName());
        return this.inspectionHandlers;
    }

    @Override
    public void verfiyAssociatedProjects() {
        for (TransformProject project : this.getProjectList()) {
            project.open();
            project.close();
        }
    }

    @Override
    public void stop() {
        if (jobLogFactory != null)
            jobLogFactory.stop();
    }

    @Override
    public Logger getLogger() {
        return this.log;
    }

    @Override
    public boolean hasLogger() {
        return this.log != null;
    }
}
