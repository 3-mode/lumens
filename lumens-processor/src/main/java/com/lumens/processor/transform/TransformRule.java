/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.AccessPath;
import com.lumens.model.Format;
import com.lumens.model.Path;
import com.lumens.model.PathToken;
import com.lumens.processor.Rule;
import java.util.Iterator;

/**
 *
 * @author shaofeng wang
 */
public class TransformRule implements Rule {

    private final Format dstFmt;
    private final TransformRuleItem root;

    public TransformRule(Format dest) {
        this.dstFmt = dest;
        this.root = new TransformRuleItem(dstFmt);
    }

    public TransformRuleItem getRootRuleItem() {
        return root;
    }

    public TransformRuleItem getRuleItem(String path) {
        Path fmtPath = new AccessPath(path);
        if (!fmtPath.isEmpty()) {
            PathToken token = null;
            TransformRuleItem parent = root;
            TransformRuleItem child = null;
            Iterator<PathToken> it = fmtPath.iterator();
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
