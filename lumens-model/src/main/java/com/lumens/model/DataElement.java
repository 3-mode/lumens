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
public class DataElement implements Element {

    protected Map<String, Element> children;
    protected List<Element> childrenList;
    protected List<Element> arrayItems;
    protected Format format;
    private Element parent;
    private int level = 0;
    private boolean isArrayItem;
    private Value value;

    public DataElement(Format format) {
        super();
        this.format = format;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void removeChild(Element child) {
        if (isField()) {
            throw new RuntimeException("Error, the data node is field");
        }

        if (isStruct()) {
            Element removed = children.remove(child.getFormat().getName());
            if (removed != null) {
                childrenList.remove(removed);
            }
        } else {
            arrayItems.remove(child);
        }
    }

    @Override
    public Element newChild(Format child) {
        Element elem = new DataElement(child);
        elem.setParent(this);
        return elem;
    }

    @Override
    public Element addChild(String name) {
        Format child = format.getChild(name);
        return addChild(new DataElement(child));
    }

    @Override
    public Element addChild(Element child) {
        if (isArray()) {
            throw new RuntimeException("Error, the data node is an array, it is not an array item");
        }
        if (children == null) {
            children = new HashMap<>();
            childrenList = new ArrayList<>();
        }
        String name = child.getFormat().getName();
        if (children.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate child \"" + format.getName() + "\"");
        }
        child.setParent(this);
        children.put(name, child);
        childrenList.add(child);
        return child;
    }

    @Override
    public Element addArrayItem() {
        return addArrayItem(newArrayItem());
    }

    @Override
    public Element addArrayItem(Element item) {
        if (!isArray()) {
            throw new RuntimeException("Error, the data node is not an array");
        }

        if (arrayItems == null) {
            arrayItems = new ArrayList<>();
        }
        item.setParent(this);
        arrayItems.add(item);
        return item;
    }

    @Override
    public Element newArrayItem() {
        if (!isArray()) {
            throw new RuntimeException("Error, the node type is not an array");
        }
        DataElement data = new DataElement(format);
        data.isArrayItem = true;
        return data;
    }

    private List<Element> getArrayItems() {
        return arrayItems;
    }

    @Override
    public void setParent(Element parent) {
        this.level = parent.isArray() ? parent.getLevel() : parent.getLevel() + 1;
        this.parent = parent;
    }

    @Override
    public Element getParent() {
        return parent;
    }

    @Override
    public Element getChild(String name) {
        return children == null ? null : children.get(name);
    }

    @Override
    public Element getChildByPath(String path) {
        return getChildByPath(new AccessPath(path));
    }

    @Override
    public Element getChildByPath(Path path) {
        Element child = this;
        PathToken token = null;
        Iterator<PathToken> it = path.iterator();
        while (it.hasNext()) {
            token = it.next();
            child = child.getChild(token.toString());

            if (child == null)
                throw new IllegalArgumentException(String.format("Error path '%s'", path.toString()));

            if (child.isArray()) {
                if (!child.hasChildren())
                    return null;

                if (token.index() >= 0)
                    child = child.getChildren().get(token.index());
            }
        }

        return child;
    }

    @Override
    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    @Override
    public List<Element> getChildren() {
        if (isArray()) {
            return getArrayItems();
        }
        return childrenList;
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public boolean isField() {
        return format.isField() || (isArrayItem() && format.isArrayOfField());
    }

    @Override
    public boolean isStruct() {
        return format.isStruct() || (isArrayItem() && format.isArrayOfStruct());
    }

    @Override
    public boolean isArray() {
        return format.isArray() && !isArrayItem();
    }

    @Override
    public boolean isArrayOfField() {
        return format.isArrayOfField();
    }

    @Override
    public boolean isArrayOfStruct() {
        return format.isArrayOfStruct();
    }

    @Override
    public boolean isArrayItem() {
        return isArrayItem;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Value)
            this.value = (Value) value;
        else
            this.value = new Value(format.getType(), value);
    }

    @Override
    public boolean isNull() {
        return value == null || value.isNull();
    }
}
