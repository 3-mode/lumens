/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Element;
import com.lumens.processor.Context;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shaofeng wang
 */
public class TransformContext implements Context
{
    private Map<String, Element> arrayIterationMap = new HashMap<String, Element>();
    private Element elementSearchEntry;
    private Element input;
    private Element result;
    private Element current;

    public TransformContext(Element input, Element result)
    {
        this.input = input;
        this.result = result;
    }

    public Element getInputElement()
    {
        return input;
    }

    public Element getResultElement()
    {
        return result;
    }

    public void setCurrentElement(Element current)
    {
        this.current = current;
    }

    public Element getCurrentElement()
    {
        return current;
    }

    public void putParentArrayElement(Element currentArrayElement, Element iterationArrayElement)
    {
        arrayIterationMap.put(currentArrayElement.toString(), iterationArrayElement);
    }

    public Element getParentArrayElement(Element currentElement)
    {
        Element arrayIterationElement = null;
        Element parent = currentElement;
        while (parent != null)
        {
            if (parent.isArrayItem() || parent.getParent() == null)
            {
                arrayIterationElement = arrayIterationMap.get(parent.toString());
                if (arrayIterationElement != null)
                    return arrayIterationElement;
            }
            parent = parent.getParent();
        }
        return null;
    }

    public void setAccessPathEntry(Element elementSearchEntry)
    {
        this.elementSearchEntry = elementSearchEntry;
    }

    @Override
    public Element getAccessPathEntry()
    {
        return elementSearchEntry;
    }
}
