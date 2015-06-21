/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public interface Element {
    /*Methods to get and set properties of data node*/

    public int getLevel();

    public void removeChild(Element child);

    public Element newChild(Format format);

    public Element addChild(String name);

    public Element addChild(Element child);

    public Element addArrayItem(Element item);

    public Element addArrayItem();

    public Element newArrayItem();

    public Element getParent();

    public Element getChild(String name);

    public Element getChildByPath(String path);

    public Element getChildByPath(Path path);

    public List<Element> getChildren();

    public boolean hasChildren();

    public Format getFormat();

    public Value getValue();

    public void setValue(Value value);

    public void setValue(Object value);

    public void setParent(Element data);

    public boolean isNull();

    public boolean isField();

    public boolean isStruct();

    public boolean isArray();

    public boolean isArrayOfField();

    public boolean isArrayOfStruct();

    public boolean isArrayItem();

    public AccessoryManager getAccessoryManager();

    public void createAccessory();

    public void passAccessory(Element srcElement);
}
