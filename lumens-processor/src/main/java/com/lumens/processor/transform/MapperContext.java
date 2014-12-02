/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import com.lumens.processor.Context;
import com.lumens.processor.transform.TransformRuleItem;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class MapperContext implements Context {
    private final TransformRuleItem rootRuleItem;
    private final Element rootSrcElement;
    private TransformRuleItem currentRuleItem;
    protected MapperContext parent;

    public MapperContext(MapperContext parent, TransformRuleItem currentRuleItem) {
        this.parent = parent;
        this.rootRuleItem = parent.getRootRuleItem();
        this.rootSrcElement = parent.getRootSourceElement();
        this.currentRuleItem = currentRuleItem;
    }

    public MapperContext(TransformRuleItem rootRuleItem, Element rootSrcElement) {
        this.rootRuleItem = rootRuleItem;
        this.rootSrcElement = rootSrcElement;
    }

    @Override
    public Context getParent() {
        return this.parent;
    }

    public TransformRuleItem getRootRuleItem() {
        return rootRuleItem;
    }

    @Override
    public Element getRootSourceElement() {
        return this.rootSrcElement;
    }

    public TransformRuleItem getCurrentRuleItem() {
        return currentRuleItem != null ? currentRuleItem : getRootRuleItem();
    }

    @Override
    public void declareVariables(Scriptable scope) {
        if (getParent() != null)
            getParent().declareVariables(scope);
    }

}
