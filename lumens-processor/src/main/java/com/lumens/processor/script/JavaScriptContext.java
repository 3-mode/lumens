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

    private static JavaScriptContext instance;
    private ScriptableObject globalScope;

    public JavaScriptContext() {
        try {
            org.mozilla.javascript.Context ctx = org.mozilla.javascript.Context.enter();
            globalScope = ctx.initStandardObjects();
            ctx.evaluateString(globalScope, ScriptUtils.loadJS("com/lumens/processor/script/build-in.js"), "build-in", 1, null);
        } catch (Exception e) {
            // TODO Process the log4j
            throw new RuntimeException("Failed to initialize JavaScript context and global scope !");
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }

    public ScriptableObject getGlobalScope() {
        return globalScope;
    }

    public static JavaScriptContext getContext() {
        return instance;
    }

    public static void start() {
        instance = new JavaScriptContext();
    }
}
