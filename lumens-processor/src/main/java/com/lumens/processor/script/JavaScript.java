/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.script;

import com.lumens.processor.transform.MapperContext;
import com.lumens.processor.Script;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;

public class JavaScript implements Script {

    private final JavaScriptBuilder builder = new JavaScriptBuilder();
    private final ScriptableObject globalScope;
    private final String sourceName;
    private final String orignalScriptText;
    private final Function jsFunction;
    private JavaScriptContext selfJsContext;

    public JavaScript(String script) throws Exception {
        this("script" + System.currentTimeMillis(), script);
    }

    public JavaScript(String sourceName, String scriptText) throws Exception {
        this(null, sourceName, scriptText);
    }

    public JavaScript(JavaScriptContext jsContext, String sourceName, String scriptText) throws Exception {
        if (jsContext == null)
            selfJsContext = jsContext = JavaScriptContext.createInstance().start();
        this.sourceName = sourceName;
        this.orignalScriptText = scriptText;
        org.mozilla.javascript.Context jsCtx = org.mozilla.javascript.Context.enter();
        this.globalScope = jsContext.getGlobalScope();
        jsFunction = jsCtx.compileFunction(globalScope, builder.build(orignalScriptText), this.sourceName, 1, null);
    }

    @Override
    public String getScriptText() {
        return orignalScriptText;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            org.mozilla.javascript.Context.exit();
            if (selfJsContext != null)
                selfJsContext.stop();
        }
    }

    @Override
    public Object execute(MapperContext ctx) {
        try {
            Object[] args = {ctx};
            // *** It is must to enter a new javascript context for current execution thread to invoke the function
            org.mozilla.javascript.Context jsCtx = org.mozilla.javascript.Context.enter();
            ctx.declareVariables(globalScope);
            Object result = jsFunction.call(jsCtx, globalScope, jsFunction, args);
            if (result instanceof NativeJavaObject) {
                NativeJavaObject nativeObj = (NativeJavaObject) result;
                return nativeObj.unwrap();
            }
            return result;
        } finally {
            ctx.removeVariables(globalScope);
            org.mozilla.javascript.Context.exit();
        }
    }
}
