package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.SQLBuilder;
import com.lumens.model.Element;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class OracleSQLBuilder implements SQLBuilder {

    @Override
    public String generateInsertSQL(Element input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateUpdateSQL(Element input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateSelectSQL(Element input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
