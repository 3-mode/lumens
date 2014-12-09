/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.ExecuteContext;
import com.lumens.engine.TransformComponent;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface Executor {

    public List<ExecuteContext> execute();

    public TransformComponent getTransformComponent();
}
