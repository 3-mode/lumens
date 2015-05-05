/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.log;

import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformEngineContext;
import com.lumens.engine.handler.DataSourceResultHandler;
import com.lumens.engine.handler.TransformerResultHandler;
import com.lumens.io.JsonUtility;
import com.lumens.logsys.LogSysFactory;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformComponentInOutLogHandler implements DataSourceResultHandler, TransformerResultHandler {
    private final Logger log = LogSysFactory.getLogger(TransformComponent.class);

    @Override
    public void processOutput(TransformComponent src, String targetName, List<Element> output) {
        if (log.isDebugEnabled())
            log.debug(String.format("Component '%s' output size '%d' target => '%s'", src.getName(), output.size(), targetName));
        processElementList(output);
    }

    @Override
    public void processInput(TransformComponent src, String targetName, List<Element> input) {
        if (log.isDebugEnabled())
            log.debug(String.format("Component '%s' input size '%d' target => '%s'", src.getName(), input.size(), targetName));
        processElementList(input);
    }

    private void processElementList(List<Element> output) {
        if (TransformEngineContext.getContext().isLogElement()) {
            try {
                for (Element e : output) {
                    JsonUtility utility = JsonUtility.createJsonUtility();
                    JsonGenerator json = utility.getGenerator();
                    json.writeStartObject();
                    new ElementSerializer(e, true).writeToJson(json);
                    json.writeEndObject();
                    log.info(utility.toUTF8String());
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
