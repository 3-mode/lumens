/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.test;

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
import org.junit.Before;
import static org.junit.Assert.*;
import com.lumens.connector.Connector;
import com.lumens.connector.ElementChunk;
import com.lumens.connector.OperationResult;
import com.lumens.connector.rapsync.RapSyncConnectorFactory;
import com.lumens.connector.rapsync.RapSyncConstants;
import static com.lumens.connector.database.DBConstants.ACTION;
import static com.lumens.connector.database.DBConstants.DATA_LENGTH;
import static com.lumens.connector.database.DBConstants.DATA_TYPE;
import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import static com.lumens.connector.database.DBConstants.WHERE;
import com.lumens.connector.rapsync.impl.Metadata;
import com.lumens.connector.rapsync.impl.Constants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RedoLogConnectorTest extends RapSyncTestBase implements RapSyncConstants, Constants {

    @Before
    public void prepareTestTable() {
        // create test table
        String schema = "LUMENS";
        String table = "FULL_SYNC";

        Metadata source = new Metadata(sourceDatabase);
        if (!source.checkTableExist(schema, table)) {
            try {
                sourceDatabase.execute(String.format("CREATE TABLE %s.%s( NAME VARCHAR2(20))", schema, table));
                sourceDatabase.execute(String.format("INSERT INTO \"%s\".\"%s\" (NAME) VALUES ('wisper')", schema, table));
                sourceDatabase.execute(String.format("INSERT INTO \"%s\".\"%s\" (NAME) VALUES ('shaofeng')", schema, table));
                sourceDatabase.execute(String.format("INSERT INTO \"%s\".\"%s\" (NAME) VALUES ('oliver')", schema, table));
                sourceDatabase.execute("commit");
            } catch (Exception ex) {
                log.error(String.format("Fail to prepare table %s.%s. Error: %s", schema, table, ex.getMessage()));
            }
        }

        // drop target table
        Metadata target = new Metadata(destinationDatabase);
        if (target.checkTableExist(schema, table)) {
            target.emptyTable(schema, table);
        } else {
            String createDDL = source.getTableDDL(schema, table);
            target.createTable(createDDL);
        }
    }

    @Test
    public void testFormat() {
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
        propsR.put(SESSION_ALTER, new Value("alter session set NLS_DATE_FORMAT='DD-MON-YYYY HH24:MI:SS'\nalter session set nls_date_language='american' "));

        Map<String, Value> propsSync = new HashMap<>();
        propsSync.put(DATABASE_DRIVER, new Value(DATABASE_DRIVER_VAL));
        propsSync.put(DATABASE_CONNECTION_URL, new Value(DATABASE_DESTINATION_URL_VAL));
        propsSync.put(DATABASE_CONNECTION_USERNAME, new Value(DATABASE_DESTINATION_USERNAME_VAL));
        propsSync.put(DATABASE_CONNECTION_PASSWORD, new Value(DATABASE_DESTINATION_PASSWORD_VAL));

        ConnectorFactory connectorFactory = new RapSyncConnectorFactory();
        Connector minerRead = connectorFactory.createConnector();
        minerRead.setPropertyList(propsR);
        minerRead.open();
        assertTrue(minerRead.isOpen());
        Map<String, Format> formatList = minerRead.getFormatList(Direction.IN);
        assertNotNull(formatList);
        minerRead.close();
    }

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
        propsR.put(SESSION_ALTER, new Value("alter session set NLS_DATE_FORMAT='DD-MON-YYYY HH24:MI:SS'\nalter session set nls_date_language='american' "));

        Map<String, Value> propsSync = new HashMap<>();
        propsSync.put(DATABASE_DRIVER, new Value(DATABASE_DRIVER_VAL));
        propsSync.put(DATABASE_CONNECTION_URL, new Value(DATABASE_DESTINATION_URL_VAL));
        propsSync.put(DATABASE_CONNECTION_USERNAME, new Value(DATABASE_DESTINATION_USERNAME_VAL));
        propsSync.put(DATABASE_CONNECTION_PASSWORD, new Value(DATABASE_DESTINATION_PASSWORD_VAL));

        ConnectorFactory connectorFactory = new RapSyncConnectorFactory();
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
        selectSQLParams.addChild(TABLE_LIST, Form.FIELD, Type.STRING);

        selectFmt.addChild(COLUMN_REDO, Form.FIELD, Type.STRING);
        selectFmt.addChild(COLUMN_SCN, Form.FIELD, Type.INTEGER);
        selectFmt.addChild(COLUMN_OPERATION, Form.FIELD, Type.STRING);
        selectFmt.addChild(COLUMN_SEG_OWNER, Form.FIELD, Type.STRING);
        selectFmt.addChild(COLUMN_TABLE_NAME, Form.FIELD, Type.STRING);
        selectFmt.addChild(COLUMN_TIMESTAMP, Form.FIELD, Type.DATE);

        Element query = new DataElement(selectFmt);
        Element sqlParams = query.addChild(SQLPARAMS);
        sqlParams.addChild(ACTION).setValue(QUERY);
        sqlParams.addChild(TABLE_LIST).setValue("FULL_SYNC,TEST");
        query.addChild(COLUMN_SEG_OWNER).setValue("='LUMENS'");
        query.addChild(COLUMN_TIMESTAMP).setValue("='06-JUN-2015 01:00:00'");
        query.addChild(COLUMN_SCN).setValue(">1664831");

        // sync format
        minerSync.start();
        Operation syncOperation = minerSync.getOperation();
        Format syncFmt = new DataFormat(FORMAT_NAME, Format.Form.STRUCT);
        Format syncSQLParams = syncFmt.addChild(SQLPARAMS, Format.Form.STRUCT);
        syncSQLParams.addChild(ACTION, Form.FIELD, Type.STRING);
        syncFmt.addChild(COLUMN_REDO, Form.FIELD, Type.STRING);
        syncFmt.addChild(COLUMN_SCN, Form.FIELD, Type.INTEGER);
        syncFmt.addChild(COLUMN_OPERATION, Form.FIELD, Type.STRING);
        syncFmt.addChild(COLUMN_SEG_OWNER, Form.FIELD, Type.STRING);
        syncFmt.addChild(COLUMN_TABLE_NAME, Form.FIELD, Type.STRING);

        List<Element> syncChunk = new ArrayList();

        try {
            OperationResult result = queryOperation.execute(new ElementChunk(Arrays.asList(query)), selectFmt);
            if (result.hasData()) {
                List<Element> redologs = result.getData();
                int max = 1000;
                System.out.println("          SCN | OPERATION | REDO SQL -----------------------------------------");
                for (Element elem : redologs) {
                    int scn = elem.getChildByPath(COLUMN_SCN).getValue().getInt();
                    String redo = elem.getChildByPath(COLUMN_REDO).getValue().toString();
                    String operation = elem.getChildByPath(COLUMN_OPERATION).getValue().toString();
                    String owner = elem.getChildByPath(COLUMN_SEG_OWNER).getValue().toString();
                    String table = elem.getChildByPath(COLUMN_TABLE_NAME).getValue().toString();
                    System.out.println("    " + scn + "  | " + operation + "  | " + redo);

                    // add data to sync
                    Element sync = new DataElement(syncFmt);
                    sync.addChild(SQLPARAMS).addChild(ACTION).setValue(SYNC);
                    sync.addChild(COLUMN_REDO).setValue(new Value(redo));
                    sync.addChild(COLUMN_SCN).setValue(new Value(scn));
                    sync.addChild(COLUMN_OPERATION).setValue(new Value(operation));
                    sync.addChild(COLUMN_SEG_OWNER).setValue(new Value(owner));
                    sync.addChild(COLUMN_TABLE_NAME).setValue(new Value(table));
                    syncChunk.add(sync);

                    if (--max < 0) {
                        break;
                    }
                }
            }

            if (syncChunk.size() > 0) {
                syncOperation.execute(new ElementChunk(syncChunk), syncFmt);
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
