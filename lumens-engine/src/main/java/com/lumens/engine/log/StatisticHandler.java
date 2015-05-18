/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.log;

import com.lumens.engine.handler.InspectionHandler;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface StatisticHandler extends InspectionHandler {

    public long statisticSuccessElement(long successCount);

    public long statisticFailureElement(long successCount);
}
