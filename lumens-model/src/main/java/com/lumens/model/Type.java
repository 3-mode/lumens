/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

/**
 *
 * @author shaofeng wang
 */
public enum Type {

    NONE("None"),
    BYTE("Byte"),
    BOOLEAN("Boolean"),
    SHORT("Short"),
    INTEGER("Integer"),
    LONG("Long"),
    FLOAT("Float"),
    DOUBLE("Double"),
    STRING("String"),
    BINARY("Binary"),
    DATE("Date");
    private final String name;

    private Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Type parseString(String type) {
        return Type.valueOf(type.toUpperCase());
    }
}
