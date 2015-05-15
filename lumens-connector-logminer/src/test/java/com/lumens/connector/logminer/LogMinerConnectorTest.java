/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Value;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Type;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import com.lumens.connector.Connector;
import com.lumens.connector.ElementChunk;
import com.lumens.connector.OperationResult;
import static com.lumens.connector.database.DBConstants.ACTION;
import static com.lumens.connector.database.DBConstants.DATA_LENGTH;
import static com.lumens.connector.database.DBConstants.DATA_TYPE;
import static com.lumens.connector.database.DBConstants.GROUPBY;
import static com.lumens.connector.database.DBConstants.ORDERBY;
import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import static com.lumens.connector.database.DBConstants.WHERE;
import com.lumens.connector.logminer.impl.Constants;
import com.lumens.connector.logminer.impl.TestBase;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerConnectorTest extends TestBase implements LogMinerConstants, Constants {

    @Test
    public void testConnectorReadSync() {
        Map<String, Value> propsR = new HashMap<>();
        propsR.put(DATABASE_DRIVER, new Value(DATABASE_DRIVER_VAL));
        propsR.put(DATABASE_CONNECTION_URL, new Value(DATABASE_SOURCE_URL_VAL));
        propsR.put(DATABASE_CONNECTION_USERNAME, new Value(DATABASE_SOURCE_USERNAME_VAL));
        propsR.put(DATABASE_CONNECTION_PASSWORD, new Value(DATABASE_SOURCE_PASSWORD_VAL));
        propsR.put(BUILD_TYPE_ONLINE, new Value(BUILD_TYPE_ONLINE));
        propsR.put(DICT_TYPE_ONLINE, new Value(DICT_TYPE_ONLINE));
        propsR.put(COMMITED_DATA_ONLY, new Value(true));
        propsR.put(NO_ROWID, new Value(true));
        propsR.put(START_SCN, new Value("0"));

        Map<String, Value> propsSync = new HashMap<>();
        propsSync.put(DATABASE_DRIVER, new Value(DATABASE_DRIVER_VAL));
        propsSync.put(DATABASE_CONNECTION_URL, new Value(DATABASE_DESTINATION_URL_VAL));
        propsSync.put(DATABASE_CONNECTION_USERNAME, new Value(DATABASE_DESTINATION_USERNAME_VAL));
        propsSync.put(DATABASE_CONNECTION_PASSWORD, new Value(DATABASE_DESTINATION_PASSWORD_VAL));
        
        ConnectorFactory connectorFactory = new LogMinerConnectorFactory();
        Connector minerRead = connectorFactory.createConnector();
        minerRead.setPropertyList(propsR);
        minerRead.open();
        assertTrue(minerRead.isOpen());
        Connector minerSync = connectorFactory.createConnector();
        minerSync.setPropertyList(propsSync);
        minerSync.open();
        assertTrue(minerSync.isOpen());
        
        Map<String, Format> formatList = minerRead.getFormatList(Direction.IN);
        assertNotNull(formatList);
        Format fmt = formatList.get(FORMAT_NAME);
        System.out.println("Redo log format name:" + fmt.getName());
        for (Format column : fmt.getChildren()) {
            System.out.println("    Column: name= " + column.getName() + " type=" + column.getProperty(DATA_TYPE) + " length=" + column.getProperty(DATA_LENGTH));
        }

        minerRead.start();
        Operation operation = minerRead.getOperation();
        Format selectFmt = new DataFormat(FORMAT_NAME, Format.Form.STRUCT);
        Format SQLParams = selectFmt.addChild(SQLPARAMS, Format.Form.STRUCT);
        SQLParams.addChild(ACTION, Form.FIELD, Type.STRING);
        SQLParams.addChild(WHERE, Form.FIELD, Type.STRING);
        SQLParams.addChild(ORDERBY, Form.FIELD, Type.STRING);
        SQLParams.addChild(GROUPBY, Form.FIELD, Type.STRING);
        selectFmt.addChild(COLUMN_REDO, Form.FIELD, Type.STRING);

        Element select = new DataElement(selectFmt);
        select.addChild(SQLPARAMS).addChild(ACTION).setValue("SELECT");
        try {
            OperationResult result = operation.execute(new ElementChunk(Arrays.asList(select)), selectFmt);
            if (result.hasData()) {
                List<Element> redologs = result.getData();
                int max = 1000;
                log.info("Reading redo log:");
                for (Element elem : redologs) {
                    System.out.println("    " + elem.getChildByPath(COLUMN_REDO).getValue().toString());
                    if (--max < 0) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            assertTrue("Fail to execute log miner query:" + ex.getMessage(), false);
        }

        minerRead.stop();
        minerRead.close();
    }
}
