/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class AccessoryManager {

    private final Map<String, Value> values = new HashMap<>();

    public Value getValue(String name) {
        return values.get(name);
    }

    public Object setValue(String name, Object value) {
        return values.put(name, new Value(value));
    }

}
