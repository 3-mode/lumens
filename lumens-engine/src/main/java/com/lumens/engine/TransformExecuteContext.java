/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.run.ExecuteContext;
import com.lumens.engine.run.ResultHandler;
import com.lumens.model.Element;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformExecuteContext implements ExecuteContext {

    private List<Element> input;
    private String targetFmtName;
    private List<ResultHandler> handlers;

    public TransformExecuteContext(String targetName) {
        this(targetName, new ArrayList<ResultHandler>(1));
    }

    public TransformExecuteContext(String targetFmtName, List<ResultHandler> handlers) {
        this(null, targetFmtName, handlers);
    }

    public TransformExecuteContext(List<Element> input, String targetFmtName, List<ResultHandler> handlers) {
        this.input = input;
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
}
