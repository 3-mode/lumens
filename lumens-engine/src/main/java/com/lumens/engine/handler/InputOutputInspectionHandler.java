/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.handler;

import com.lumens.engine.TransformComponent;
import com.lumens.model.Element;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public interface InputOutputInspectionHandler extends InspectionHandler {

    public void processOutput(TransformComponent src, String targetName, List<Element> output);

    public void processInput(TransformComponent src, String targetName, List<Element> input);
}
