/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.log;

import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformEngineContext;
import com.lumens.engine.TransformException;
import com.lumens.engine.handler.ExceptionHandler;
import com.lumens.engine.handler.InputOutputInspectionHandler;
import com.lumens.engine.handler.JobLogHandler;
import com.lumens.engine.handler.ProjectInspectionHandler;
import com.lumens.io.JsonUtility;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.processor.transform.MapperException;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class FileJobLogHandler implements ProjectInspectionHandler, JobLogHandler, ExceptionHandler, InputOutputInspectionHandler {
    private final Logger log;
    private String projectName;
    private long projectID;

    public FileJobLogHandler(Logger log) {
        this.log = log;
    }

    @Override
    public ProjectInspectionHandler withProjectID(long projectID) {
        this.projectID = projectID;
        return this;
    }

    @Override
    public ProjectInspectionHandler withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    @Override
    public void handleExceptionOnElement(Exception e) {
        if (e instanceof TransformException) {
            TransformException te = (TransformException) e;
            TransformComponent tc = te.getComponentOfException();
            Throwable t = te.getCause();
            if (t instanceof MapperException) {
                try {
                    MapperException me = (MapperException) t;
                    Element elem = me.getElementOfException();
                    JsonUtility utility = JsonUtility.createJsonUtility();
                    JsonGenerator json = utility.getGenerator();
                    json.writeStartArray();
                    {
                        json.writeStartObject();
                        {
                            json.writeObjectFieldStart("project");
                            {
                                json.writeNumberField("id", projectID);
                                json.writeStringField("name", projectName);
                            }
                            json.writeEndObject();
                        }
                        json.writeEndObject();
                        json.writeStartObject();
                        {
                            json.writeObjectFieldStart("component");
                            {
                                json.writeStringField("id", tc.getId());
                                json.writeStringField("name", tc.getName());
                            }
                            json.writeEndObject();
                        }
                        json.writeEndObject();
                        {
                            json.writeStartObject();
                            json.writeStringField("element", toJsonString(elem));
                            json.writeEndObject();
                        }
                    }
                    json.writeEndArray();
                    log.error(utility.toUTF8String());
                } catch (Exception ex) {
                    log.error(ex);
                }
            }
        }
    }

    private String toJsonString(Element elem) {
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            new ElementSerializer(elem, true).writeToJson(json);
            json.writeEndObject();
            return utility.toUTF8String();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void processOutput(TransformComponent src, String targetName, List<Element> output) {
        if (log.isDebugEnabled())
            log.debug(String.format("Component '%s' output size '%d' target => '%s'", src.getName(), output != null ? output.size() : 0, targetName));
        else if (TransformEngineContext.getContext().isLogElement())
            log.info(String.format("Component '%s' output size '%d' target => '%s'", src.getName(), output != null ? output.size() : 0, targetName));
    }

    @Override
    public void processInput(TransformComponent src, String targetName, List<Element> input) {
        if (log.isDebugEnabled())
            log.debug(String.format("Component '%s' input size '%d' target => '%s'", src.getName(), input != null ? input.size() : 0, targetName));
        else if (TransformEngineContext.getContext().isLogElement())
            log.info(String.format("Component '%s' input size '%d' target => '%s'", src.getName(), input != null ? input.size() : 0, targetName));
    }
}
