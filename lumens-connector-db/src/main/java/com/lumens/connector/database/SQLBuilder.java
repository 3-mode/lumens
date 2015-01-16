/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.model.Element;
import com.lumens.model.Format;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public interface SQLBuilder {

    public Format getFormat();

    public String generateInsertSQL(Element input);

    public String generateUpdateSQL(Element input);

    public String generateSelectSQL(Element input);

    public String generateDeleteSQL(Element input);
}
