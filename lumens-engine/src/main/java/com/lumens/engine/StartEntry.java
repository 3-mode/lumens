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
    private String startEntryName;

    public StartEntry(String name, TransformComponent component) {
        this.startEntryName = name;
        this.tComponent = component;
    }

    /**
     * @return the tComponent
     */
    public TransformComponent getStartComponent() {
        return tComponent;
    }

    /**
     * @return the startEntryName
     */
    public String getStartName() {
        return startEntryName;
    }
}
