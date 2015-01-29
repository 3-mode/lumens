/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.ElementChunk;
import com.lumens.engine.component.resource.DataContext;
import com.lumens.engine.handler.ResultHandler;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformExecuteContext implements ExecuteContext {

    private List<ResultHandler> handlers;
    private final TransformComponent target;
    private final String targetFmtName;
    private final ExecuteContext parentCtx;
    private final List<ExecuteContext> children = new ArrayList<>();
    private final ElementChunk chunk;

    public TransformExecuteContext(String targetName) {
        this(null, targetName, new ArrayList<ResultHandler>(1));
    }

    public TransformExecuteContext(TransformComponent target, String targetFmtName, List<ResultHandler> handlers) {
        this(null, null, target, targetFmtName, handlers);
    }

    public TransformExecuteContext(ExecuteContext parentCtx, ElementChunk chunk, TransformComponent target, String targetFmtName, List<ResultHandler> handlers) {
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
    public List<ResultHandler> getResultHandlers() {
        return this.handlers;
    }

    public TransformExecuteContext addHandler(ResultHandler handler) {
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
