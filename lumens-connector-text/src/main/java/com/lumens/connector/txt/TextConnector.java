/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.Connector;
import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.Direction;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextConnector implements Connector, TextConstants {
    private FormatBuilder formatBuilder;
    private TextClient textClient;
    private boolean isOpen = false;

    Map<String, Value> props;
    private String schemaPath;

    @Override
    public void setPropertyList(Map<String, Value> props) {
        this.props = props;

        if (props.containsKey(SCHEMA_PATH)) {
            schemaPath = props.get(SCHEMA_PATH).getString();
        }
        String delimiter = props.get(FILEDELIMITER).getString();
        String escape = props.get(ESCAPE_CHAR).getString();
        if (delimiter.equalsIgnoreCase(escape)) {
            throw new RuntimeException("Delimiter should not be equal to escape char.");
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    // To craete specific client in type of DOM or SAX 
    @Override
    public void open() {
        if (textClient == null) {
            textClient = new TextClient(this, props);
            formatBuilder = new TextFormatBuilder(schemaPath);
            try {
                formatBuilder.initalize();
                isOpen = true;
            } catch (RuntimeException ex) {
                throw ex;
            }
        }
    }

    @Override
    public void close() {
        textClient = null;
        formatBuilder = null;
        isOpen = false;
    }

    @Override
    public Operation getOperation() {
        return new TextOperation(textClient);
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        return formatBuilder.getFormatList(direction);
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        return formatBuilder.getFormat(format, path, direction);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
