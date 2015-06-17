/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
class FormatUtils {

    public static final String STATUS = "$Status";
    public static final String CODE = "Code";
    public static final String MESSAGE = "Message";

    public static Format buildStautsFormat(Format format) {
        if (format == null)
            return null;
        if (format.getChild(STATUS) != null)
            return format.getChild(STATUS);

        Format status = format.addChild(STATUS, Format.Form.STRUCT);
        status.addChild(CODE, Format.Form.FIELD, Type.INTEGER);
        status.addChild(MESSAGE, Format.Form.FIELD, Type.STRING);
        return status;
    }
}
