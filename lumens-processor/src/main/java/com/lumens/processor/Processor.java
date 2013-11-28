/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.model.Element;

public interface Processor
{
    public void setName(String name);

    public String getName();

    public Object execute(Rule rule, Element input);
}
