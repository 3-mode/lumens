/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.ElementChunk;
import com.lumens.engine.component.resource.DataContext;
import com.lumens.engine.handler.InspectionHander;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.wang@outlook.com)
 */
public interface ExecuteContext {

    public String getTargetFormatName();

    public ElementChunk getInput();

    public List<InspectionHander> getInspectionHandlers();

    public TransformComponent getTargetComponent();

    public ExecuteContext getParentContext();

    public DataContext getParentDataContext();
}
