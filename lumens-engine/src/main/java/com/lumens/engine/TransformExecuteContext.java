/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.run.ResultHandler;
import com.lumens.model.Element;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformExecuteContext implements ExecuteContext {

    private List<ResultHandler> handlers;
    private final List<Element> input;
    private final TransformComponent target;
    private final String targetFmtName;
    private final ExecuteContext parentCtx;
    private final List<ExecuteContext> children = new ArrayList<>();

    public TransformExecuteContext(String targetName) {
        this(null, targetName, new ArrayList<ResultHandler>(1));
    }

    public TransformExecuteContext(TransformComponent target, String targetFmtName, List<ResultHandler> handlers) {
        this(null, null, target, targetFmtName, handlers);
    }

    public TransformExecuteContext(ExecuteContext parentCtx, List<Element> input, TransformComponent target, String targetFmtName, List<ResultHandler> handlers) {
        this.parentCtx = parentCtx;
        this.input = input;
        this.target = target;
        this.targetFmtName = targetFmtName;
        this.handlers = handlers;
    }

    @Override
    public List<Element> getInput() {
        return input;
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
    public void addChildContext(ExecuteContext child) {
        this.children.add(child);
    }

    @Override
    public void removeChildContext(ExecuteContext child) {
        this.children.remove(child);
    }

    @Override
    public List<ExecuteContext> getChildrenContext() {
        return children;
    }

    @Override
    public ExecuteContext getParentContext() {
        return parentCtx;
    }
}
