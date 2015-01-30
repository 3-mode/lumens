/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class StartEntry {

    private final TransformComponent tComponent;
    private final String startFormatName;

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

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return "StartEntry{" + "tComponent=" + tComponent.getId() + ", startFormatName=" + startFormatName + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        final StartEntry other = (StartEntry) obj;
        return other.getStartFormatName().equals(getStartFormatName())
               && other.getStartComponent().getId().equals(getStartComponent().getId());
    }
}
