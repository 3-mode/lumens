package com.lumens.log;

/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
import com.lumens.engine.TransformComponent;
import com.lumens.engine.handler.DataSourceResultHandler;
import com.lumens.engine.handler.TransformerResultHandler;
import com.lumens.sysdb.DAOFactory;
import com.lumens.sysdb.dao.InOutLogDAO;
import com.lumens.sysdb.entity.InOutLogItem;
import com.lumens.io.JsonUtility;
import java.sql.Timestamp;
import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import com.lumens.io.Utils;
import com.lumens.model.Element;
import com.lumens.model.serializer.ElementSerializer;

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
            for (Element e : eList) {
                InOutLogItem item = new InOutLogItem();
                item.logID = Utils.generateID();
                item.componentID = Long.parseLong(src.getId());
                item.componentName = src.getName();
                item.projectID = this.projectID;
                item.projectName = this.projectName;
                item.direction = direction;
                item.targetName = targetName;
                item.lastModifTime = new Timestamp(System.currentTimeMillis());
                JsonUtility utility = JsonUtility.createJsonUtility();
                JsonGenerator json = utility.getGenerator();
                json.writeStartObject();
                new ElementSerializer(e, true).writeToJson(json);
                json.writeEndObject();
                item.data = utility.toUTF8String();
                inoutLogDAO.create(item);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
