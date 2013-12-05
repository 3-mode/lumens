/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import com.lumens.processor.Pair;

/**
 *
 * @author shaofeng wang
 */
public class TransformPair extends Pair<Element, TransformRuleItem> {

    public TransformPair(Element element, TransformRuleItem ruleItem) {
        super(element, ruleItem);
    }
}
