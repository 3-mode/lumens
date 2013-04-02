/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

/**
 *
 * @author shaofeng wang
 */
public abstract class AbstractProcessor implements Processor
{
    private String name;

    public AbstractProcessor()
    {
        name = "Processor-" + this.hashCode();
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
