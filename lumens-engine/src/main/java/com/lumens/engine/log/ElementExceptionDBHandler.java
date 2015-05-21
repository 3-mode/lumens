/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.log;

import com.lumens.connector.Direction;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformException;
import com.lumens.engine.handler.ExceptionHandler;
import com.lumens.engine.handler.ProjectInspectionHandler;
import com.lumens.io.JsonUtility;
import com.lumens.io.Utils;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.processor.transform.MapperException;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.ElementExceptionDAO;
import com.lumens.sysdb.entity.ElementExceptionLog;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ElementExceptionDBHandler implements ExceptionHandler {
    private final long jobID;
    private long projectID;
    private String projectName;

    public ElementExceptionDBHandler(long jobID) {
        this.jobID = jobID;
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
                MapperException me = (MapperException) t;
                Element elem = me.getElementOfException();
                ElementExceptionDAO eeDAO = DAOFactory.getElementExceptionDAO();
                ElementExceptionLog eeLog = new ElementExceptionLog(Utils.generateID(), jobID,
                                                                    me.fillInStackTrace().toString().replace("'", "''"),
                                                                    Long.parseLong(tc.getId()), tc.getName(),
                                                                    projectID, projectName,
                                                                    Direction.IN.name(), "", toJsonString(elem),
                                                                    System.currentTimeMillis());
                eeDAO.create(eeLog);
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
            String result = utility.toUTF8String();
            if (StringUtils.isNotEmpty(result))
                return result.replace("'", "''");
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
