/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.Element;
import java.util.List;

public interface Processor {

    public void setName(String name);

    public String getName();

    public Object execute(Rule rule, List<Element> input);
}
