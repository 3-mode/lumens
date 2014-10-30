package com.lumens.connector.database;

import com.lumens.model.Element;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public interface SQLBuilder {

    public String generateInsertSQL(Element input);

    public String generateUpdateSQL(Element input);

    public String generateSelectSQL(Element input);

    public String generateDeleteSQL(Element input);
}
