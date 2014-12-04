/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public interface Format {

    public enum Form {

        NONE("None"),
        FIELD("Field"),
        STRUCT("Struct"),
        ARRAYOFFIELD("ArrayOfField"),
        ARRAYOFSTRUCT("ArrayOfStruct");
        private final String name;

        private Form(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Form parseString(String form) {
            return Form.valueOf(form.toUpperCase());
        }
    }

    public Type getType();

    public Form getForm();

    public void setType(Type type);

    public void setForm(Form form);

    public String getName();

    public void setName(String name);

    public Format getParent();

    public void setParent(Format format);

    public void setProperty(String name, Value value);

    public Value getProperty(String name);

    public Map<String, Value> getPropertyList();

    public Format addChild(Format format);

    public Format addChild(String name, Form form, Type type);

    public Format addChild(String name, Form form);

    public Format getChild(String name);

    public Format getChildByPath(String path);

    public Format getChildByPath(Path path);

    public List<Format> getChildren();

    public Path getFullPath();

    public boolean isField();

    public boolean isStruct();

    public boolean isArray();

    public boolean isArrayOfField();

    public boolean isArrayOfStruct();

    public Format depthCopy();
}
