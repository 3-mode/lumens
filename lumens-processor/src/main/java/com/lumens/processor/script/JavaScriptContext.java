/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.script;

import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class JavaScriptContext {

    private ScriptableObject globalScope;
    private boolean isStarted;

    public ScriptableObject getGlobalScope() {
        return globalScope;
    }

    public JavaScriptContext start() {
        try {
            org.mozilla.javascript.Context ctx = org.mozilla.javascript.Context.enter();
            globalScope = ctx.initStandardObjects();
            ctx.evaluateString(globalScope, ScriptUtils.loadJS("com/lumens/processor/script/build-in.js"), "build-in", 1, null);
            isStarted = true;
            return this;
        } catch (Exception e) {
            // TODO Process the log4j
            throw new RuntimeException("Failed to initialize JavaScript context and global scope !");
        }
    }

    public void stop() {
        if (isStarted)
            org.mozilla.javascript.Context.exit();
        isStarted = false;
    }

    public static JavaScriptContext createInstance() {
        return new JavaScriptContext();
    }
}
