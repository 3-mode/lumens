/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Format;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface FormatBuilder {

    public void initalize();

    public Map<String, Format> getFormatList(Direction direction);

    public Format getFormat(Format format, String path, Direction direction);
}