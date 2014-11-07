/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.script;

import com.lumens.processor.Context;
import com.lumens.processor.Script;
import com.lumens.processor.transform.TransformContext;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JavaScript implements Script {

    private ScriptableObject globalScope;
    private final JavaScriptBuilder builder = new JavaScriptBuilder();
    private final String orignalScriptText;
    private final Scriptable currentScope;
    private Function jsFunction;

    public JavaScript(String script) throws Exception {
        this("script" + System.currentTimeMillis(), script);
    }

    public JavaScript(String sourceName, String scriptText) throws Exception {
        this.orignalScriptText = scriptText;
        this.globalScope = JavaScriptContext.getContext().getGlobalScope();
        org.mozilla.javascript.Context jsCTX = org.mozilla.javascript.Context.enter();
        currentScope = jsCTX.initStandardObjects(globalScope);
        jsFunction = jsCTX.compileFunction(currentScope, builder.build(orignalScriptText), sourceName, 1, null);
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
            if (ctx instanceof TransformContext) {
                Object[] args = {
                    ctx
                };
                // Enter a new context for current execution thread to invoke the function
                org.mozilla.javascript.Context jsCTX = org.mozilla.javascript.Context.enter();
                Scriptable scope = jsCTX.initStandardObjects(globalScope);
                Object result = jsFunction.call(jsCTX, scope, scope, args);
                if (result instanceof NativeJavaObject) {
                    NativeJavaObject nativeObj = (NativeJavaObject) result;
                    return nativeObj.unwrap();
                }
                return result;
            }
            return null;
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }
}
