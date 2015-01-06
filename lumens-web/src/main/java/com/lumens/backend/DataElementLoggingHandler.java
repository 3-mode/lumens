/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.engine.TransformComponent;
import com.lumens.engine.handler.DataSourceResultHandler;
import com.lumens.engine.handler.TransformerResultHandler;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DataElementLoggingHandler implements DataSourceResultHandler, TransformerResultHandler {
    private final long projectID;

    public DataElementLoggingHandler(long projectID) {
        this.projectID = projectID;
    }

    @Override
    public void processOutput(TransformComponent src, String targetName, List<Element> output) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new ElementSerializer(output.get(0), true).writeToJson(baos);
            ApplicationContext.get().cacheResultString(baos.toString());
        } catch (Exception ex) {
        }
    }

    @Override
    public void processInput(TransformComponent src, String targetName, List<Element> input) {
    }

}
