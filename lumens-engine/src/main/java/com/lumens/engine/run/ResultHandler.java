/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.run;

import com.lumens.engine.TransformComponent;
import com.lumens.model.Element;
import java.util.List;

public interface ResultHandler {

    public void process(TransformComponent src, String resultName, List<Element> results);
}
