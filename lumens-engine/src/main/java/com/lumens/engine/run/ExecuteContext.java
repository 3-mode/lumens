/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface ExecuteContext {

    public String getTargetFormatName();

    public List<Element> getInput();

    public List<ResultHandler> getResultHandlers();
}
