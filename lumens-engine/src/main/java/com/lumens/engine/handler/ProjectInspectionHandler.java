/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.handler;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface ProjectInspectionHandler extends InspectionHandler {

    public ProjectInspectionHandler withProjectID(long projectID);

    public ProjectInspectionHandler withProjectName(String projectName);
}
