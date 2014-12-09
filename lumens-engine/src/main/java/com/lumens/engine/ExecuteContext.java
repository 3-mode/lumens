/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.run.ResultHandler;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.wang@outlook.com)
 */
public interface ExecuteContext {

    public String getTargetFormatName();

    public List<Element> getInput();

    public List<ResultHandler> getResultHandlers();

    public TransformComponent getTargetComponent();

    public void addChildContext(ExecuteContext child);

    public void removeChildContext(ExecuteContext child);

    public List<ExecuteContext> getChildrenContext();

    public ExecuteContext getParentContext();
}
