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
    protected boolean isOpen;
    private String description;
    private final String id;
    private final String compType;
    private final Map<String, TransformComponent> sourceList = new HashMap<>();
    private final Map<String, TransformComponent> targetList = new HashMap<>();

    public AbstractTransformComponent(String compType, String id) {
        this.compType = compType;
        this.id = id;
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
    public String getComponentType() {
        return compType;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void sourceFrom(TransformComponent source) {
        if (!sourceList.containsKey(source.getId()))
            sourceList.put(source.getId(), source);
    }

    @Override
    public void targetTo(TransformComponent target) {
        target.sourceFrom(this);
        if (!targetList.containsKey(target.getId()))
            targetList.put(target.getId(), target);
    }

    @Override
    public Map<String, TransformComponent> getTargetList() {
        return targetList;
    }

    @Override
    public Map<String, TransformComponent> getSourceList() {
        return sourceList;
    }

    @Override
    public boolean hasTarget() {
        return !targetList.isEmpty();
    }

    @Override
    public boolean hasSource() {
        return !sourceList.isEmpty();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
}
