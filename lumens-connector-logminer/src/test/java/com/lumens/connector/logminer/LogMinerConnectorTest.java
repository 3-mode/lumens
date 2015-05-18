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
import java.util.ArrayList;

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

        // query format
        minerRead.start();
        Operation queryOperation = minerRead.getOperation();
        Format selectFmt = new DataFormat(FORMAT_NAME, Format.Form.STRUCT);
        Format selectSQLParams = selectFmt.addChild(SQLPARAMS, Format.Form.STRUCT);
        selectSQLParams.addChild(ACTION, Form.FIELD, Type.STRING);
        selectSQLParams.addChild(WHERE, Form.FIELD, Type.STRING);
        selectSQLParams.addChild(ORDERBY, Form.FIELD, Type.STRING);
        selectSQLParams.addChild(GROUPBY, Form.FIELD, Type.STRING);
        selectFmt.addChild(COLUMN_REDO, Form.FIELD, Type.STRING);
        selectFmt.addChild(COLUMN_SCN, Form.FIELD, Type.INTEGER);

        Element query = new DataElement(selectFmt);
        query.addChild(SQLPARAMS).addChild(ACTION).setValue(QUERY);

        // sync format
        minerSync.start();
        Operation syncOperation = minerSync.getOperation();
        Format syncFmt = new DataFormat(FORMAT_NAME, Format.Form.STRUCT);
        Format syncSQLParams = syncFmt.addChild(SQLPARAMS, Format.Form.STRUCT);
        syncSQLParams.addChild(ACTION, Form.FIELD, Type.STRING);
        syncFmt.addChild(COLUMN_REDO, Form.FIELD, Type.STRING);
        syncFmt.addChild(COLUMN_SCN, Form.FIELD, Type.INTEGER);
        syncFmt.addChild(COLUMN_OPERATION, Form.FIELD, Type.STRING);

        List<Element> syncChunk = new ArrayList();

        try {
            OperationResult result = queryOperation.execute(new ElementChunk(Arrays.asList(query)), selectFmt);
            if (result.hasData()) {
                List<Element> redologs = result.getData();
                int max = 1000;
                System.out.println("          SCN | OPERATION | REDO SQL -----------------------------------------");
                for (Element elem : redologs) {
                    String scn = elem.getChildByPath(COLUMN_SCN).getValue().toString();
                    String redo = elem.getChildByPath(COLUMN_REDO).getValue().toString();
                    String operation = elem.getChildByPath(COLUMN_OPERATION).getValue().toString();
                    System.out.println("    " + scn + "  | " + operation + "  | " + redo);

                    // add data to sync
                    Element sync = new DataElement(syncFmt);
                    sync.addChild(SQLPARAMS).addChild(ACTION).setValue(SYNC);
                    sync.addChild(COLUMN_REDO).setValue(new Value(redo));
                    sync.addChild(COLUMN_SCN).setValue(new Value(scn));;
                    sync.addChild(COLUMN_OPERATION).setValue(new Value(operation));;
                    syncChunk.add(sync);

                    if (--max < 0) {
                        break;
                    }
                }
            }
            
            if (syncChunk.size() > 0){
                //syncOperation.execute(new ElementChunk(syncChunk), syncFmt);
            }
        } catch (Exception ex) {
            assertTrue("Fail to execute log miner query:" + ex.getMessage(), false);
        }

        minerRead.stop();
        minerRead.close();
        minerSync.stop();
        minerSync.close();
    }
}
