/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor;

import com.lumens.processor.transform.MapperContext;

public interface Script {

    public Object execute(MapperContext ctx);
    
    public String getScriptText();
}
