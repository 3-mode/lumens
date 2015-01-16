/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database;

import com.lumens.connector.Direction;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface Client {

    public int getPageSize();

    public void open();

    public void close();

    public Map<String, Format> getFormatList(Direction direction, boolean fullLoad);

    public Format getFormat(Format format);

    public void execute(String SQL);

    public List<Element> executeQuery(String generatePageSQL, Format output);

    public void commit();
}
