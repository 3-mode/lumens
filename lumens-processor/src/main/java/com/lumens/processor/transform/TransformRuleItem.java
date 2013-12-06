/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Format;
import com.lumens.processor.Script;
import com.lumens.processor.script.AccessPathScript;
import com.lumens.processor.script.JavaScript;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class TransformRuleItem {

    private TransformRuleItem parent;
    private List<TransformRuleItem> children;
    private Script script;
    private String orignalScriptText;
    private String arrayIterationPath;
    private Format format;

    TransformRuleItem(Format format) {
        this.format = format;
    }

    public void setScript(String scriptText) throws Exception {
        orignalScriptText = scriptText;
        String trimedScriptText = scriptText.trim();
        if (TransformUtils.isPathFormat(trimedScriptText))
            this.script = new AccessPathScript(TransformUtils.getAccessPath(trimedScriptText));
        else
            this.script = new JavaScript(format.getFullPath().toString(), trimedScriptText);
    }

    public Script getScript() {
        return script;
    }

    public String getScriptString() {
        return orignalScriptText;
    }

    public void setArrayIterationPath(String arrayIterationPath) {
        if (!format.isArray() && format.getParent() != null) {
            throw new IllegalArgumentException("iteration path \"" + arrayIterationPath + "\" can not be configured for a no array or no root element");
        }

        TransformRuleItem item = parent;
        while (item != null) {
            if (item.arrayIterationPath != null && item.arrayIterationPath.equalsIgnoreCase(arrayIterationPath))
                throw new IllegalArgumentException("iteration path \"" + arrayIterationPath + "\" already is configured in its parent element");
            item = item.parent;
        }
        ArrayDeque<TransformRuleItem> items = new ArrayDeque<>();
        List<TransformRuleItem> currentChildren = getChildren();
        if (currentChildren != null && !currentChildren.isEmpty())
            items.addAll(currentChildren);

        while (!items.isEmpty()) {
            item = items.removeFirst();
            if (item.arrayIterationPath != null && item.arrayIterationPath.equalsIgnoreCase(arrayIterationPath))
                throw new IllegalArgumentException("iteration path \"" + arrayIterationPath + "\" already is configured in its child element");
            currentChildren = item.getChildren();
            if (currentChildren != null && !currentChildren.isEmpty())
                items.addAll(currentChildren);
        }

        this.arrayIterationPath = arrayIterationPath;
    }

    public String getArrayIterationPath() {
        return arrayIterationPath;
    }

    public TransformRuleItem getParent() {
        return parent;
    }

    public Iterator<TransformRuleItem> iterator() {
        return children != null ? children.iterator() : null;
    }

    public TransformRuleItem getChild(String name) {
        if (children != null) {
            for (TransformRuleItem item : children)
                if (item.format.getName().equalsIgnoreCase(name))
                    return item;
        }
        return null;
    }

    public TransformRuleItem addChild(String name) {
        if (children == null)
            children = new ArrayList<>();
        Format child = format.getChild(name);
        if (child == null)
            throw new IllegalArgumentException("The child format \"" + name + "\" does not exist");
        TransformRuleItem item = new TransformRuleItem(child);
        item.parent = this;
        children.add(item);
        return item;
    }

    public Format getFormat() {
        return format;
    }

    List<TransformRuleItem> getChildren() {
        return children;
    }
}
