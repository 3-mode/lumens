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
    private String identifier;
    private String description;
    private Map<String, TransformComponent> sourceList = new HashMap<>();
    private Map<String, TransformComponent> targetList = new HashMap<>();

    public AbstractTransformComponent(String identifier) {
        this.identifier = identifier;
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
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void sourceFrom(TransformComponent source) {
        if (!sourceList.containsKey(source.getName()))
            sourceList.put(source.getName(), source);
    }

    @Override
    public void targetTo(TransformComponent target) {
        target.sourceFrom(this);
        if (!targetList.containsKey(target.getName()))
            targetList.put(target.getName(), target);
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
