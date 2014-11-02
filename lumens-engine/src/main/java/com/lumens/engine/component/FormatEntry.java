/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.connector.Direction;
import com.lumens.model.Format;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class FormatEntry {

    private Format format;
    private Direction direction;
    private String name;
    private String dataSourceId;

    public FormatEntry(String dataSourceId, String name, Format format) {
        this(dataSourceId, name, format, null);
    }

    public FormatEntry(String dataSourceName, String name, Format format, Direction direction) {
        this.dataSourceId = dataSourceName;
        this.name = name;
        this.format = format;
        this.direction = direction;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
