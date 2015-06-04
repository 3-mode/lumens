/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Format;
import com.lumens.model.Type;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class StatusUtils {

    public static void buildStautsFormat(Format format) {
        if (format == null)
            return;
        Format status = format.addChild("Status", Format.Form.STRUCT);
        status.addChild("Code", Format.Form.FIELD, Type.INTEGER);
        status.addChild("Message", Format.Form.FIELD, Type.STRING);
    }
}
