/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import com.lumens.processor.ProcessorUtils;

/**
 *
 * @author shaofeng wang
 */
public class TransformUtils extends ProcessorUtils {

    public static boolean checkTransformParameters(Object... args) {
        if (args == null || args.length < 1)
            return false;
        if (!(args[0] instanceof TransformRule)
            || (args.length == 2 && (args[1] != null && !(args[1] instanceof Element))))
            return false;

        return true;
    }
}
