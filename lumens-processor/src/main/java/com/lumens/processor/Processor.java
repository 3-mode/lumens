/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

public interface Processor {

    public void setName(String name);

    public String getName();

    public Object execute(ProcessorContext processorCtx);

    public void start();

    public void stop();
}
