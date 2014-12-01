/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ForeachMapperContext extends MapperContext {
    private final TransformForeach foreach;
    private final Element sourceElement;
    private final int currentIndex;

    public ForeachMapperContext(MapperContext ctx, TransformForeach foreach, int currentIndex, Element sourceElement) {
        super(ctx.getRootRuleItem(), ctx.getRootSourceElement());
        this.parent = ctx;
        this.foreach = foreach;
        this.currentIndex = currentIndex;
        this.sourceElement = sourceElement;
    }

    public TransformForeach getForeach() {
        return foreach;
    }

    public Element getSourceElement() {
        return this.sourceElement;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    @Override
    public void declareVariables(Scriptable scope) {
        if (getParent() != null)
            getParent().declareVariables(scope);
        scope.put(getForeach().getIndexName(), scope, getCurrentIndex());
    }

}
