/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.xml;

import com.lumens.connector.Connector;
import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.Direction;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author whiskey
 */
public class XmlConnector implements Connector {

    private boolean isOpen = false;
    private Map<String, Format> formatListIn;
    private Map<String, Format> formatListOut;
    private FormatBuilder formatBuilder;
    private Map<String, Format> xmlFmt;
    private XmlClient xml;
    // xml properties 
    private String version;
    private String encoding;
    private String path;

    public void setPropertyList(Map<String, Value> props) {
        if (props.containsKey(XmlConstants.VERSION))
            version = props.get(XmlConstants.VERSION).getString();

        if (props.containsKey(XmlConstants.ENCODING))
            encoding = props.get(XmlConstants.ENCODING).getString();

        if (props.containsKey(XmlConstants.PATH))
            path = props.get(XmlConstants.PATH).getString();
    }

    public boolean isOpen() {
        return isOpen;
    }

    // To craete specific client in type of DOM or SAX 
    public void open() {
        if (xml == null) {
            xml = new XmlClient(this);
        }
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }

    public Operation getOperation() {
        return new XmlOperation();
    }

    public Map<String, Format> getFormatList(Direction direction) {
        return null;
    }

    public Format getFormat(Format format, String path, Direction direction) {
        return null;
    }
    // xml connector special mathods
}
