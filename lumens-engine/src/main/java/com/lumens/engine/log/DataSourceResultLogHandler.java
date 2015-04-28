/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.log;

import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformEngineContext;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.handler.DataSourceResultHandler;
import com.lumens.logsys.LogSysFactory;
import com.lumens.model.Element;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DataSourceResultLogHandler implements DataSourceResultHandler {
    private final Logger log = LogSysFactory.getLogger(DataSource.class);

    @Override
    public void processOutput(TransformComponent src, String targetName, List<Element> output) {
        if (log.isDebugEnabled())
            log.debug(String.format("Datasource '%s' output size '%d' target => '%s'", src.getName(), output.size(), targetName));

        if (TransformEngineContext.getContext().isLogElement()) {

        }
    }

    @Override
    public void processInput(TransformComponent src, String targetName, List<Element> input) {
        if (log.isDebugEnabled())
            log.debug(String.format("Datasource '%s' input size '%d' target => '%s'", src.getName(), input.size(), targetName));

        if (TransformEngineContext.getContext().isLogElement()) {

        }
    }
}
