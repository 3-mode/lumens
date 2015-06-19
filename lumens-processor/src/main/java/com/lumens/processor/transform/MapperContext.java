/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author shaofeng wang
 */
public interface MapperContext {

    public Element getRootSourceElement();

    public MapperContext getParent();

    public void declareVariables(Scriptable scope);

    public void removeVariables(Scriptable scope);
}
