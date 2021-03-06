/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.handler;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface ExceptionHandler extends ProjectInspectionHandler {
    public void handleExceptionOnElement(Exception e);
}
