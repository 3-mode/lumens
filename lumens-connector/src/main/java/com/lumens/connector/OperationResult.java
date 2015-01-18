/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector;

import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface OperationResult {

    public boolean has();

    public List<Element> get();

    public boolean hasNext();

    public OperationResult next();
}
