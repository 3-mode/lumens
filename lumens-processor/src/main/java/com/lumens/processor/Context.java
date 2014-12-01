/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.Element;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author shaofeng wang
 */
public interface Context {
    public Element getRootSourceElement();

    public Context getParent();

    public void declareVariables(Scriptable scope);
}
