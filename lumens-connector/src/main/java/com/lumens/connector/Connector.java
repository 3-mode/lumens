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
public interface Connector extends Configurable {

    public boolean isOpen();

    public void open();

    public void close();

    public Operation getOperation();

    public Map<String, Format> getFormatList(Direction direction);

    public Format getFormat(Format format, String path, Direction direction);

    public void start();

    public void stop();
}
