/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import com.lumens.processor.Context;
import java.util.HashMap;
import java.util.Map;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author shaofeng wang
 */
public class TransformContext implements Context {

    private final Map<String, Element> arrayIterationMap = new HashMap<>();
    private final Element input;
    private final Element result;
    private Element elementSearchEntry;
    private Element current;

    public TransformContext(Element input, Element result) {
        this.input = input;
        this.elementSearchEntry = input;
        this.result = result;
    }

    @Override
    public Element getRootSourceElement() {
        return elementSearchEntry;
    }

    public Element getInputElement() {
        return input;
    }

    public Element getResultElement() {
        return result;
    }

    public void setCurrentElement(Element current) {
        this.current = current;
    }

    public Element getCurrentElement() {
        return current;
    }

    public void putParentArrayElement(Element currentArrayElement, Element iterationArrayElement) {
        arrayIterationMap.put(currentArrayElement.toString(), iterationArrayElement);
    }

    public Element getParentArrayElement(Element currentElement) {
        Element arrayIterationElement = null;
        Element parent = currentElement;
        while (parent != null) {
            if (parent.isArrayItem() || parent.getParent() == null) {
                arrayIterationElement = arrayIterationMap.get(parent.toString());
                if (arrayIterationElement != null) {
                    return arrayIterationElement;
                }
            }
            parent = parent.getParent();
        }
        return null;
    }

    public void setAccessPathEntry(Element elementSearchEntry) {
        this.elementSearchEntry = elementSearchEntry;
    }

    public void setCurrentShortSourcePath(String shortSoourcePath) {
    }

    @Override
    public Context getParent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void declareVariables(Scriptable scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
