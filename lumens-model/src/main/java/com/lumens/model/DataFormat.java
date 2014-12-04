/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class DataFormat implements Format {

    private Type type = Type.NONE;
    private Form form = Form.NONE;

    protected Map<String, Value> propertyList;
    protected Map<String, Format> childMap;
    protected List<Format> childList;
    private Format parent;
    private String name;

    public DataFormat() {
    }

    public DataFormat(String name) {
        this.name = name;
    }

    public DataFormat(String name, Form form) {
        this.name = name;
        this.form = form;
    }

    public DataFormat(String name, Form form, Type type) {
        this.name = name;
        this.form = form;
        this.type = type;
    }

    protected Format copy() {
        DataFormat copied = new DataFormat(getName(), getForm(), getType());
        if (propertyList != null) {
            copied.propertyList = new HashMap<>(propertyList);
        }
        return copied;
    }

    @Override
    public Format depthCopy() {
        Format copied = copy();
        if (childList != null) {
            for (Format child : childList) {
                copied.addChild(child.depthCopy());
            }
        }

        return copied;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Form getForm() {
        return form;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void setForm(Form form) {
        this.form = form;
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
    public void setProperty(String name, Value value) {
        if (propertyList == null) {
            propertyList = new HashMap<>();
        }
        propertyList.put(name, value);
    }

    @Override
    public Value getProperty(String name) {
        return propertyList == null ? null : propertyList.get(name);
    }

    @Override
    public Map<String, Value> getPropertyList() {
        return propertyList;
    }

    @Override
    public Format addChild(Format format) {
        if (childMap == null && childList == null) {
            childMap = new HashMap<>();
            childList = new ArrayList<>();
        } else if (childMap.containsKey(format.getName())) {
            throw new IllegalArgumentException("Duplicate child \"" + format.getName() + "\"");
        }
        format.setParent(this);

        childMap.put(format.getName(), format);
        childList.add(format);
        return format;
    }

    @Override
    public Format addChild(String name, Form form, Type type) {
        return addChild(new DataFormat(name, form, type));
    }

    @Override
    public Format addChild(String name, Form form) {
        return addChild(new DataFormat(name, form, Type.NONE));
    }

    @Override
    public Format getChild(String name) {
        return childMap == null ? null : childMap.get(name);
    }

    @Override
    public Format getChildByPath(String path) {
        return getChildByPath(new AccessPath(path));
    }

    @Override
    public Format getChildByPath(Path path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        Iterator<PathToken> it = path.iterator();
        Format format = this;
        while (format != null && it.hasNext()) {
            format = format.getChild(it.next().toString());
        }
        return format;
    }

    @Override
    public List<Format> getChildren() {
        return childList;
    }

    @Override
    public Path getFullPath() {
        // The path includes root node name also. example: "Employee.Address.city"
        // The "Employee" is the root node name
        Path fullPath = new AccessPath((String) null);
        if (parent != null) {
            Format format = this;
            while (format != null) {
                fullPath.addLeft(format.getName());
                format = format.getParent();
            }
        }
        return fullPath;
    }

    @Override
    public Format getParent() {
        return parent;
    }

    @Override
    public void setParent(Format parent) {
        this.parent = parent;
    }

    @Override
    public boolean isField() {
        return form == Form.FIELD;
    }

    @Override
    public boolean isStruct() {
        return form == Form.STRUCT;
    }

    @Override
    public boolean isArray() {
        return form == Form.ARRAYOFFIELD || form == Form.ARRAYOFSTRUCT;
    }

    @Override
    public boolean isArrayOfField() {
        return form == Form.ARRAYOFFIELD;
    }

    @Override
    public boolean isArrayOfStruct() {
        return form == Form.ARRAYOFSTRUCT;
    }
}
