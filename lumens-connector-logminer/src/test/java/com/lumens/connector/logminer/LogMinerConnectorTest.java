/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer;

import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import com.lumens.connector.Connector;
import static com.lumens.connector.database.DBConstants.DATA_LENGTH;
import static com.lumens.connector.database.DBConstants.DATA_TYPE;
import com.lumens.connector.logminer.impl.TestBase;
import java.util.HashMap;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class LogMinerConnectorTest  extends TestBase implements LogMinerConstants{

    @Test
    public void testConnectorRead() {
        Map<String, Value> propsR = new HashMap<>();
        propsR.put(DATABASE_DRIVER, new Value(DATABASE_DRIVER_VAL));
        propsR.put(DATABASE_SOURCE_URL, new Value(DATABASE_SOURCE_URL_VAL));
        propsR.put(DATABASE_SOURCE_USERNAME, new Value(DATABASE_SOURCE_USERNAME_VAL));
        propsR.put(DATABASE_SOURCE_PASSWORD, new Value(DATABASE_SOURCE_PASSWORD_VAL));
        propsR.put(BUILD_TYPE_ONLINE, new Value(BUILD_TYPE_ONLINE));
        propsR.put(DICT_TYPE_ONLINE, new Value(DICT_TYPE_ONLINE));
        propsR.put(COMMITED_DATA_ONLY, new Value(true));
        propsR.put(NO_ROWID, new Value(true));
        propsR.put(START_SCN, new Value("0"));
                
        ConnectorFactory cntr = new LogMinerConnectorFactory();
        Connector miner = cntr.createConnector();
        miner.setPropertyList(propsR);
        miner.open();
        assertTrue(miner.isOpen());
        
        Map<String, Format> formatList = miner.getFormatList(Direction.IN);
        assertNotNull(formatList);
        Format fmt = formatList.get(FORMAT_NAME);
        System.out.println("Redo log format name:" + fmt.getName());
        for(Format column: fmt.getChildren()){
            System.out.println("Column: name= " + column.getName() + " type=" + column.getProperty(DATA_TYPE) + " length=" + column.getProperty(DATA_LENGTH));            
        }
    }
}
