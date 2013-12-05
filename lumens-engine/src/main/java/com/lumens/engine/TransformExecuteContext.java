/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.run.ExecuteContext;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformExecuteContext implements ExecuteContext {

    private Object input;
    private String targetName;

    public TransformExecuteContext(String targetName) {
        //TODO
        this(null, targetName);
    }

    public TransformExecuteContext(Object input, String targetName) {
        this.input = input;
        this.targetName = targetName;
    }

    @Override
    public Object getInput() {
        return input;
    }

    @Override
    public String getTargetName() {
        return targetName;
    }
}
