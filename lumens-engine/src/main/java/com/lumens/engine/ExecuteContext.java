/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.component.resource.DataContext;
import com.lumens.engine.handler.ResultHandler;
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

    public ExecuteContext getParentContext();

    public DataContext getParentDataContext();
}
