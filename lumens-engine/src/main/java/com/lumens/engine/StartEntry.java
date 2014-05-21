/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class StartEntry {

    private TransformComponent tComponent;
    private String startFormatName;

    public StartEntry(String formatName, TransformComponent component) {
        this.startFormatName = formatName;
        this.tComponent = component;
    }

    /**
     * @return the tComponent
     */
    public TransformComponent getStartComponent() {
        return tComponent;
    }

    /**
     * @return the startFormatName
     */
    public String getStartFormatName() {
        return startFormatName;
    }
}
