/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ProcessorExecutionContext implements ProcessorContext {
    private final Rule rule;
    private final List<Element> input;

    public ProcessorExecutionContext(Rule rule, List<Element> input) {
        this.rule = rule;
        this.input = input;
    }

    @Override
    public Rule getRule() {
        return this.rule;
    }

    @Override
    public List<Element> getInput() {
        return this.input;
    }

}
