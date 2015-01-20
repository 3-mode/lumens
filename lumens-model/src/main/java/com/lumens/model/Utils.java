/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class Utils {
    public static boolean isNullValue(Element value) {
        return value == null || value.getValue() == null;
    }
}
