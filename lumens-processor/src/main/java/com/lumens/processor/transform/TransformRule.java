/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.AccessPath;
import com.lumens.model.Format;
import com.lumens.model.Path;
import com.lumens.model.PathToken;
import com.lumens.processor.Rule;
import com.lumens.processor.script.JavaScriptContext;
import java.util.Iterator;

/**
 *
 * @author shaofeng wang
 */
public class TransformRule implements Rule {

    private final Format dstFmt;
    private final TransformRuleItem root;
    private final JavaScriptContext jsContext;

    public TransformRule(Format dest) {
        this.dstFmt = dest;
        this.jsContext = JavaScriptContext.createInstance().start();
        this.root = new TransformRuleItem(this, dstFmt);
    }

    public JavaScriptContext getJavaScriptContext() {
        return this.jsContext;
    }

    public TransformRuleItem getRootRuleItem() {
        return root;
    }

    public TransformRuleItem getRuleItem(String fullPath) {
        Path fmtPath = new AccessPath(fullPath);
        if (!root.getFormat().getName().equals(fmtPath.token(0).toString()))
            throw new RuntimeException(String.format("Root format name '%s' doesn't match with root '%s' from path",
                                                     root.getFormat().getName(), fmtPath.token(0).toString()));
        if (!fmtPath.isEmpty()) {
            PathToken token = null;
            TransformRuleItem parent = root;
            TransformRuleItem child = parent;
            Iterator<PathToken> it = fmtPath.removeLeft(1).iterator();
            while (it.hasNext()) {
                token = it.next();
                child = parent.getChild(token.toString());
                if (child == null) {
                    child = parent.addChild(token.toString());
                }
                parent = child;
            }
            return child;
        }

        return root;
    }

    @Override
    public String getName() {
        return dstFmt.getName();
    }
}
