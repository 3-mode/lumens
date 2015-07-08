/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.CarrierManager;
import com.lumens.model.Element;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformMapperContext implements MapperContext {
    private final TransformRuleItem rootRuleItem;
    private final Element rootSourceElement;
    private TransformRuleItem currentRuleItem;
    private CarrierManager carrierManager;
    protected TransformMapperContext parent;

    public TransformMapperContext(TransformMapperContext parent, TransformRuleItem currentRuleItem) {
        this.parent = parent;
        this.rootRuleItem = parent.getRootRuleItem();
        this.rootSourceElement = parent.getRootSourceElement();
        this.currentRuleItem = currentRuleItem;
    }

    public TransformMapperContext(TransformRuleItem rootRuleItem, Element rootSrcElement) {
        this.rootRuleItem = rootRuleItem;
        this.rootSourceElement = rootSrcElement;
        this.carrierManager = (rootSrcElement != null && rootSrcElement.getCarrierManager() != null)
                            ? rootSrcElement.getCarrierManager() : new CarrierManager();
    }

    @Override
    public MapperContext getRoot() {
        MapperContext root = this;
        while (root.getParent() != null && root.getParent() != this) {
            root = root.getParent();
        }
        return root;
    }

    @Override
    public CarrierManager getCarrierManager() {
        return this.carrierManager;
    }

    @Override
    public MapperContext getParent() {
        return this.parent;
    }

    public TransformRuleItem getRootRuleItem() {
        return rootRuleItem;
    }

    @Override
    public Element getRootSourceElement() {
        return this.rootSourceElement;
    }

    public TransformRuleItem getCurrentRuleItem() {
        return currentRuleItem != null ? currentRuleItem : getRootRuleItem();
    }

    @Override
    public void declareVariables(Scriptable scope) {
        if (getParent() != null)
            getParent().declareVariables(scope);
    }

    @Override
    public void removeVariables(Scriptable scope) {
        if (getParent() != null)
            getParent().removeVariables(scope);
    }
}
