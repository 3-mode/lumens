/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component.resource;

import com.lumens.connector.OperationResult;
import com.lumens.connector.ElementChunk;
import com.lumens.engine.ExecuteContext;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.handler.InspectionHandler;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DataContext implements ExecuteContext {
    private final ExecuteContext parentCtx;
    private final OperationResult operateResult;
    private final List<ExecuteContext> children = new ArrayList<>();

    public DataContext(ExecuteContext context, OperationResult operateResult) {
        this.parentCtx = context;
        this.operateResult = operateResult;
    }

    @Override
    public String getTargetFormatName() {
        return parentCtx.getTargetFormatName();
    }

    @Override
    public ElementChunk getInput() {
        return parentCtx.getInput();
    }

    @Override
    public List<InspectionHandler> getInspectionHandlers() {
        return parentCtx.getInspectionHandlers();
    }

    @Override
    public TransformComponent getTargetComponent() {
        return parentCtx.getTargetComponent();
    }

    public OperationResult getResult() {
        return operateResult;
    }

    @Override
    public ExecuteContext getParentContext() {
        return parentCtx;
    }

    @Override
    public DataContext getParentDataContext() {
        return this;
    }
}
