/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.log;

import com.lumens.connector.Direction;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformEngineContext;
import com.lumens.engine.handler.InputOutputInspectionHandler;
import com.lumens.io.JsonUtility;
import com.lumens.io.Utils;
import com.lumens.logsys.LogSysFactory;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.InOutLogDAO;
import com.lumens.sysdb.entity.InOutLogItem;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformComponentDBLogHandler implements InputOutputInspectionHandler {
    private final Logger log = LogSysFactory.getLogger(TransformComponent.class);
    private final long projectID;
    private final String projectName;

    public TransformComponentDBLogHandler(long projectID, String projectName) {
        this.projectID = projectID;
        this.projectName = projectName;
    }

    @Override
    public void processOutput(TransformComponent src, String targetName, List<Element> output) {
        processElementList(src, targetName, Direction.OUT, output);
    }

    @Override
    public void processInput(TransformComponent src, String targetName, List<Element> input) {
        processElementList(src, targetName, Direction.IN, input);
    }

    private void processElementList(TransformComponent src, String targetName, Direction direction, List<Element> eList) {
        if (TransformEngineContext.getContext().isLogElement() && eList != null) {
            InOutLogDAO inoutLogDAO = DAOFactory.getInOutLogDAO();
            for (Element e : eList) {
                try {
                    JsonUtility utility = JsonUtility.createJsonUtility();
                    JsonGenerator json = utility.getGenerator();
                    json.writeStartObject();
                    new ElementSerializer(e, true).writeToJson(json);
                    json.writeEndObject();
                    InOutLogItem item = new InOutLogItem(Utils.generateID(), Long.parseLong(src.getId()), src.getName(),
                                                         this.projectID, this.projectName, direction.name(), targetName,
                                                         utility.toUTF8String(),
                                                         System.currentTimeMillis());
                    inoutLogDAO.create(item);
                } catch (Exception ex) {
                    log.error(ex);
                }
            }
        }
    }
}
