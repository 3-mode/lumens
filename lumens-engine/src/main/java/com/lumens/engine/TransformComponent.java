/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface TransformComponent {

    public void setX(int x);

    public void setY(int y);

    public int getX();

    public int getY();

    public void targetTo(TransformComponent target);

    public void sourceFrom(TransformComponent target);

    public boolean hasTarget();

    public boolean hasSource();

    public boolean isSingleTarget();

    public Map<String, TransformComponent> getTargetList();

    public Map<String, TransformComponent> getSourceList();

    public boolean isOpen();

    public void open() throws Exception;

    public void close();

    public List<ExecuteContext> execute(ExecuteContext context);

    public boolean accept(String name);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public String getComponentType();

    public String getId();
}
