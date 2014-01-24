/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.engine.TransformComponent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public abstract class AbstractTransformComponent implements TransformComponent {

    private int x;
    private int y;
    private String name;
    private String className;
    private String description;
    private Map<String, TransformComponent> targetList;
    protected boolean isOpen;

    public AbstractTransformComponent(String className) {
        this.className = className;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void targetTo(TransformComponent target) {
        if (targetList == null)
            targetList = new HashMap<>();
        targetList.put(target.getName(), target);
    }

    @Override
    public Map<String, TransformComponent> getTargetList() {
        return targetList;
    }

    @Override
    public boolean hasTarget() {
        return targetList != null && !targetList.isEmpty();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
}
