/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.script;

import com.lumens.processor.Context;
import com.lumens.processor.Script;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JavaScript implements Script {

    private ScriptableObject globalScope;
    private final JavaScriptBuilder builder = new JavaScriptBuilder();
    private final String sourceName;
    private final String orignalScriptText;
    private final Scriptable scriptScope;
    private Function jsFunction;
    private final Scriptable funcScope;

    public JavaScript(String script) throws Exception {
        this("script" + System.currentTimeMillis(), script);
    }

    public JavaScript(String sourceName, String scriptText) throws Exception {
        this.sourceName = sourceName;
        this.orignalScriptText = scriptText;
        this.globalScope = JavaScriptContext.getContext().getGlobalScope();
        org.mozilla.javascript.Context jsCtx = org.mozilla.javascript.Context.enter();
        scriptScope = jsCtx.initStandardObjects(globalScope);
        funcScope = jsCtx.newObject(scriptScope);
        jsFunction = jsCtx.compileFunction(funcScope, builder.build(orignalScriptText), this.sourceName, 1, null);
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
        try {
            Object[] args = {
                ctx
            };
            // Enter a new context for current execution thread to invoke the function
            org.mozilla.javascript.Context jsCtx = org.mozilla.javascript.Context.enter();
            Scriptable scope = jsCtx.initStandardObjects(globalScope);
            ctx.declareVariables(scope);
            Object result = jsFunction.call(jsCtx, scope, scope, args);
            if (result instanceof NativeJavaObject) {
                NativeJavaObject nativeObj = (NativeJavaObject) result;
                return nativeObj.unwrap();
            }
            return result;
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }
}
