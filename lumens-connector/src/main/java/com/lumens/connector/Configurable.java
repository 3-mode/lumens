/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface Configurable
{
    public void setPropertyList(Map<String, Value> parameters);
}
