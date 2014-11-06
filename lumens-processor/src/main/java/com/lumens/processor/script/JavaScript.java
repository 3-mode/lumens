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
    private final String sourceName;
    private Function jsFunction;

    public JavaScript(String script) throws Exception {
        this("script" + System.currentTimeMillis(), script);
    }

    public JavaScript(String sourceName, String scriptText) throws Exception {
        // TODO refine here, put the context initialization to gloabl place, in
        // order to load build in function only once
        this.globalScope = JavaScriptContext.getContext().getGlobalScope();
        this.sourceName = sourceName;
        this.orignalScriptText = scriptText;
    }

    @Override
    public Object execute(Context ctx) {
        try {
            if (ctx instanceof TransformContext) {
                Object[] args = {
                    ctx
                };
                org.mozilla.javascript.Context jsCTX = org.mozilla.javascript.Context.enter();
                Scriptable scope = jsCTX.initStandardObjects(globalScope);
                jsFunction = jsCTX.compileFunction(scope, builder.build(orignalScriptText), sourceName, 1, null);
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
