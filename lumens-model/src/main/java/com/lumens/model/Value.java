/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import java.util.Date;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class Value {

    protected Object value;
    protected Type type;

    public Value(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Value(Type type, String value) {
        this.type = type;
        set(value);
    }

    public Value(Type type) {
        this(type, null);
    }

    public Value() {
        this(Type.NONE, null);
    }

    public Value(short value) {
        this.type = Type.SHORT;
        this.value = value;
    }

    public Value(byte value) {
        this.type = Type.BYTE;
        this.value = value;
    }

    public Value(boolean value) {
        this.type = Type.BOOLEAN;
        this.value = value;
    }

    public Value(int value) {
        this.type = Type.INTEGER;
        this.value = value;
    }

    public Value(long value) {
        this.type = Type.LONG;
        this.value = value;
    }

    public Value(float value) {
        this.type = Type.FLOAT;
        this.value = value;
    }

    public Value(double value) {
        this.type = Type.DOUBLE;
        this.value = value;
    }

    public Value(byte[] value) {
        this.type = Type.BINARY;
        this.value = value;
    }

    public Value(Date value) {
        this.type = Type.DATE;
        this.value = value;
    }

    public Value(String value) {
        this.type = Type.STRING;
        this.value = value;
    }

    public void set(Object value) {
        if ((isBoolean() && value instanceof Boolean)
            || (isByte() && value instanceof Byte)
            || (isShort() && value instanceof Short)
            || (isInt() && value instanceof Integer)
            || (isLong() && value instanceof Long)
            || (isFloat() && value instanceof Float)
            || (isDouble() && value instanceof Double)
            || (isDate() && value instanceof Date)
            || (isBinary() && value instanceof byte[])
            || (isString() && value instanceof String)) {
            this.value = value;
        } else if (value != null && value instanceof String) {
            this.value = parseString(value.toString());
        } else {
            throw new IllegalArgumentException(String.format("Error, data type is '%s', value type is '%s' !", type(), value));
        }
    }

    public void set(short value) {
        if (!isShort()) {
            throw new IllegalArgumentException("Error, data type is not short !");
        }
        this.value = value;
    }

    public void set(byte value) {
        if (!isByte()) {
            throw new IllegalArgumentException("Error, data type is not Byte !");
        }
        this.value = value;
    }

    public void set(boolean value) {
        if (!isBoolean()) {
            throw new IllegalArgumentException("Error, data type is not Boolean !");
        }
        this.value = value;
    }

    public void set(int value) {
        if (!isInt()) {
            throw new IllegalArgumentException("Error, data type is not int !");
        }
        this.value = value;
    }

    public void set(long value) {
        if (!isLong()) {
            throw new IllegalArgumentException("Error, data type is not long !");
        }
        this.value = value;
    }

    public void set(float value) {
        if (!isFloat()) {
            throw new IllegalArgumentException("Error, data type is not float !");
        }
        this.value = value;
    }

    public void set(double value) {
        if (!isDouble()) {
            throw new IllegalArgumentException("Error, data type is not double !");
        }
        this.value = value;
    }

    public void set(byte[] value) {
        if (!isBinary()) {
            throw new IllegalArgumentException("Error, data type is not binary !");
        }
        this.value = value;
    }

    public void set(Date value) {
        if (!isDate()) {
            throw new IllegalArgumentException("Error, data type is not date time !");
        }
        this.value = value;
    }

    public void set(String value) {
        // TODO need to try to convert to other data type from a string
        if (!isString()) {
            this.value = parseString(value);
        } else {
            this.value = value;
        }
    }

    public Object get() {
        return value;
    }

    public short getShort() {
        return (Short) value;
    }

    public byte getByte() {
        return (Byte) value;
    }

    public boolean getBoolean() {
        return (Boolean) value;
    }

    public int getInt() {
        return (Integer) value;
    }

    public long getLong() {
        return (Long) value;
    }

    public float getFloat() {
        return (Float) value;
    }

    public double getDouble() {
        return (Double) value;
    }

    public byte[] getBinary() {
        return (byte[]) value;
    }

    public String getString() {
        return toString();
    }

    @Override
    public String toString() {
        return value == null ? null : value.toString();
    }

    public Date getDate() {
        return (Date) value;
    }

    public boolean isShort() {
        return type() == Type.SHORT;
    }

    public boolean isByte() {
        return type() == Type.BYTE;
    }

    public boolean isBoolean() {
        return type() == Type.BOOLEAN;
    }

    public boolean isInt() {
        return type() == Type.INTEGER;
    }

    public boolean isLong() {
        return type() == Type.LONG;
    }

    public boolean isFloat() {
        return type() == Type.FLOAT;
    }

    public boolean isDouble() {
        return type() == Type.DOUBLE;
    }

    public boolean isBinary() {
        return type() == Type.BINARY;
    }

    public boolean isDate() {
        return type() == Type.DATE;
    }

    public boolean isString() {
        return type() == Type.STRING;
    }

    public boolean isNull() {
        return value == null;
    }
    
    private Object parseString(String value) {
        if (isBoolean()) {
            return Boolean.parseBoolean(value);
        } else if (isByte()) {
            return Byte.parseByte(value);
        } else if (isShort()) {
            return Short.parseShort(value);
        } else if (isInt()) {
            return Integer.parseInt(value);
        } else if (isLong()) {
            return Long.parseLong(value);
        } else if (isFloat()) {
            return Float.parseFloat(value);
        } else if (isDouble()) {
            return Double.parseDouble(value);
        } else if (isDate()) {
            return DateTime.parse(value);
        } else if (isBinary()) {
            return Base64.decodeBase64(value);
        }

        throw new RuntimeException("Not supported data type !");
    }

    public Type type() {
        return type;
    }
}
