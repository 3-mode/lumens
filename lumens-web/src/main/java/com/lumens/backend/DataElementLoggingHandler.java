/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.backend.sql.DAOFactory;
import com.lumens.backend.sql.dao.InOutLogDAO;
import com.lumens.backend.sql.entity.InOutLogItem;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.handler.DataSourceResultHandler;
import com.lumens.engine.handler.TransformerResultHandler;
import com.lumens.io.JsonUtility;
import com.lumens.model.DateTime;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.serializer.ElementSerializer;
import com.lumens.model.serializer.FormatSerializer;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DataElementLoggingHandler implements DataSourceResultHandler, TransformerResultHandler {
    private final long projectID;
    private final String projectName;

    public DataElementLoggingHandler(long projectID, String projectName) {
        this.projectID = projectID;
        this.projectName = projectName;
    }

    @Override
    public void processOutput(TransformComponent src, String targetName, List<Element> output) {
        this.processElementList(src, targetName, "OUT", output);
    }

    @Override
    public void processInput(TransformComponent src, String targetName, List<Element> input) {
        this.processElementList(src, targetName, "IN", input);
    }

    private void processElementList(TransformComponent src, String targetName, String direction, List<Element> eList) {
        try {
            if (eList == null || eList.isEmpty())
                return;
            InOutLogDAO inoutLogDAO = DAOFactory.getInOutLogDAO();
            InOutLogItem item = new InOutLogItem();
            item.logID = ServerUtils.generateID();
            item.componentID = Long.parseLong(src.getId());
            item.componentName = src.getName();
            item.projectID = this.projectID;
            item.projectName = this.projectName;
            item.direction = direction;
            item.targetName = targetName;
            DateFormat sf = DateTime.DATETIME_PATTERN[0];
            String date = sf.format(Calendar.getInstance().getTime());
            item.lastModifTime = Timestamp.valueOf(date);
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeArrayFieldStart("element_list");
            for (Element e : eList) {
                json.writeStartObject();
                new ElementSerializer(e, true).writeToJson(json);
                json.writeEndObject();
            }
            json.writeEndArray();
            json.writeEndObject();
            item.data = utility.toUTF8String();
            inoutLogDAO.create(item);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
