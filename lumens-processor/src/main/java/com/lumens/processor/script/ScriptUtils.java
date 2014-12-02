/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.script;

import com.lumens.model.Element;
import com.lumens.processor.Context;
import com.lumens.processor.transform.ForeachMapperContext;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author shaofeng wang
 */
public class ScriptUtils {

    public static String loadJS(String name) throws Exception {
        InputStream in = getInputStream(name);
        try {
            return IOUtils.toString(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static Object getElement(Context ctx, String path) {
        AccessPathScript script = new AccessPathScript(path);
        return script.execute(ctx);
    }

    private static InputStream getInputStream(String name) throws Exception {
        return ScriptUtils.class.getClassLoader().getResourceAsStream(name);
    }

    public static Element getStartElement(Context ctx) {
        Element rootSrcElement;
        Context currentCtx = ctx;
        // Search the parent for each context from current node
        while (currentCtx != null) {
            if (currentCtx instanceof ForeachMapperContext) {
                ForeachMapperContext foreachCtx = (ForeachMapperContext) currentCtx;
                rootSrcElement = foreachCtx.getSourceElement();
                return rootSrcElement;
            }
            currentCtx = currentCtx.getParent();
        }
        return ctx.getRootSourceElement();
    }
}
