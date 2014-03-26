/*
 * (C) Copyright Hewlett-Packard Development Company, L.P. All Rights Reserved.
 */
package com.lumens.processor.script;

import com.lumens.processor.Context;
import com.lumens.processor.Script;
import com.lumens.processor.transform.TransformContext;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JavaScript implements Script {

    private static ScriptableObject globalScope;

    static {
        try {
            org.mozilla.javascript.Context ctx = org.mozilla.javascript.Context.enter();
            globalScope = ctx.initStandardObjects();
            ctx.evaluateString(globalScope, ScriptUtils.loadJS("com/lumens/processor/script/build-in.js"), "build-in", 1, null);
        } catch (Exception e) {
            // TODO Process the log4j
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }
    private final JavaScriptBuilder builder = new JavaScriptBuilder();
    private final String orignalScriptText;
    private Scriptable scope;
    private org.mozilla.javascript.Context jsCTX;
    private Function jsFunction;

    public JavaScript(String script) throws Exception {
        this("script" + System.currentTimeMillis(), script);
    }

    public JavaScript(String sourceName, String scriptText) throws Exception {
        // TODO refine here, put the context initialization to gloabl place, in
        // order to load build in function only once
        this.orignalScriptText = scriptText;
        jsCTX = org.mozilla.javascript.Context.enter();
        scope = jsCTX.initStandardObjects(globalScope);
        jsFunction = jsCTX.compileFunction(scope, builder.build(orignalScriptText), sourceName, 1, null);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }

    @Override
    public Object execute(Context ctx) {
        if (ctx instanceof TransformContext) {
            Object[] args = {
                ctx
            };
            Object result = jsFunction.call(jsCTX, scope, scope, args);
            if (result instanceof NativeJavaObject) {
                NativeJavaObject nativeObj = (NativeJavaObject) result;
                return nativeObj.unwrap();
            }
            return result;
        }
        return null;
    }
}
