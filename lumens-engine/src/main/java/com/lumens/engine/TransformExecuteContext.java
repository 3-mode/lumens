/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.ElementChunk;
import com.lumens.engine.component.resource.DataContext;
import com.lumens.engine.handler.InspectionHandler;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformExecuteContext implements ExecuteContext {

    private List<InspectionHandler> handlers;
    private final TransformComponent target;
    private final String targetFmtName;
    private final ExecuteContext parentCtx;
    private final ElementChunk chunk;

    public TransformExecuteContext(String targetName) {
        this(null, targetName, new ArrayList<InspectionHandler>(1));
    }

    public TransformExecuteContext(TransformComponent target, String targetFmtName, List<InspectionHandler> handlers) {
        this(null, null, target, targetFmtName, handlers);
    }

    public TransformExecuteContext(ExecuteContext parentCtx, ElementChunk chunk, TransformComponent target, String targetFmtName, List<InspectionHandler> handlers) {
        this.parentCtx = parentCtx;
        this.chunk = chunk;
        this.target = target;
        this.targetFmtName = targetFmtName;
        this.handlers = handlers;
    }

    @Override
    public ElementChunk getInput() {
        return chunk == null ? new ElementChunk(null) : chunk;
    }

    @Override
    public String getTargetFormatName() {
        return targetFmtName;
    }

    @Override
    public List<InspectionHandler> getInspectionHandlers() {
        return this.handlers;
    }

    public TransformExecuteContext addHandler(InspectionHandler handler) {
        this.handlers.add(handler);
        return this;
    }

    @Override
    public TransformComponent getTargetComponent() {
        return target;
    }

    @Override
    public ExecuteContext getParentContext() {
        return parentCtx;
    }

    @Override
    public DataContext getParentDataContext() {
        ExecuteContext parent = this;
        while (parent != null && !(parent instanceof DataContext))
            parent = parent.getParentContext();
        return parent != null && parent instanceof DataContext ? (DataContext) parent : null;
    }
}
