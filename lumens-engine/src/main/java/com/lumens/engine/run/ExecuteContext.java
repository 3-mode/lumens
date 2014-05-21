/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface ExecuteContext {

    public String getTargetFormatName();

    public Object getInput();

    public List<ResultHandler> getResultHandlers();
}
