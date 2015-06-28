/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.Format;
import com.lumens.processor.ProcessorUtils;
import com.lumens.processor.Script;
import com.lumens.processor.script.AccessPathScript;
import com.lumens.processor.script.JavaScript;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shaofeng wang
 */
public class TransformRuleItem {
    private final TransformRule rule;
    private final Format format;
    private final List<TransformForeach> foreachList = new ArrayList<>();
    private TransformRuleItem parent;
    private List<TransformRuleItem> children;
    private String orignalScriptText;
    private Script script;

    TransformRuleItem(TransformRule rule, Format format) {
        this.rule = rule;
        this.format = format;
    }

    public void setScript(String scriptText) {
        try {
            orignalScriptText = scriptText == null ? "" : scriptText;
            String trimedScriptText = orignalScriptText.trim();
            if (ProcessorUtils.isPathFormat(trimedScriptText)) {
                this.script = new AccessPathScript(ProcessorUtils.getAccessPath(trimedScriptText));
            } else {
                this.script = new JavaScript(rule.getJavaScriptContext(), format.getFullPath().toString(), trimedScriptText);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Script getScript() {
        return script;
    }

    public String getScriptString() {
        return orignalScriptText;
    }

    public void addTransformForeach(TransformForeach foreach) {
        this.foreachList.add(foreach);
    }

    public void removeTransformForeach(String fullPath) {
        Iterator<TransformForeach> it = foreachList.iterator();
        while (it.hasNext()) {
            TransformForeach foreach = it.next();
            if (foreach.hasSourcePath() && foreach.getSourcePath().equals(fullPath))
                it.remove();
        }
    }

    public List<TransformForeach> getTransformForeach() {
        return this.foreachList;
    }

    public TransformRuleItem getParent() {
        return parent;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public List<TransformRuleItem> getChildren() {
        return children;
    }

    public TransformRuleItem getChild(String name) {
        if (children != null) {
            for (TransformRuleItem item : children) {
                if (item.format.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        }
        return null;
    }

    public TransformRuleItem addChild(String name) {
        if (children == null) {
            children = new ArrayList<>();
        }
        Format child = format.getChild(name);
        if (child == null) {
            throw new IllegalArgumentException("The child format \"" + name + "\" does not exist");
        }
        TransformRuleItem item = new TransformRuleItem(rule, child);
        item.parent = this;
        children.add(item);
        return item;
    }

    public Format getFormat() {
        return format;
    }
}
