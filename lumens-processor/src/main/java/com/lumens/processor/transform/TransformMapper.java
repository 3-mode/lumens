/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import com.lumens.processor.AbstractProcessor;
import com.lumens.processor.Rule;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformMapper extends AbstractProcessor {
    @Override
    public Object execute(Rule rule, Element input) {
        if (rule instanceof TransformRule) {
            TransformRule transformRule = (TransformRule) rule;
        }
        throw new RuntimeException("Unsupported input data !");
    }
}
