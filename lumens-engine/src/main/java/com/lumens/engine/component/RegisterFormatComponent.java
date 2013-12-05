/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.connector.Direction;
import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface RegisterFormatComponent {

    public void registerFormat(String formatEntryName, Format format, Direction direction);

    public FormatEntry removeFormat(String formatEntryName, Direction direction);

    public Map<String, FormatEntry> getRegisteredFormatList(Direction direction);
}
