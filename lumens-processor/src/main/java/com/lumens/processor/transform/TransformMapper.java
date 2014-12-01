/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform;

import com.lumens.model.AccessPath;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Path;
import com.lumens.model.Type;
import com.lumens.processor.AbstractProcessor;
import com.lumens.processor.Rule;
import com.lumens.processor.Script;
import com.lumens.processor.script.ScriptUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformMapper extends AbstractProcessor {

    @Override
    public Object execute(Rule rule, Element input) {
        if (rule instanceof TransformRule) {
            TransformRule transformRule = (TransformRule) rule;
            Element rootSourceElement = input;
            List<Element> results = new ArrayList<>();
            TransformRuleItem rootRuleItem = transformRule.getRootRuleItem();
            TransformForeach foreach = rootRuleItem.getTransformForeach();
            // Handle root element for each to generate output element list
            if (foreach != null && foreach.hasSourcePath()) {
                // The source path format should be "RootName.Child1_name.Child2_name...." or "RootName.Array_Child1_name[0].Child2_name...."
                Path foreachSourcePath = new AccessPath(foreach.getSourcePath());
                if (!rootSourceElement.getFormat().getName().equals(foreachSourcePath.token(0).toString()))
                    throw new RuntimeException(String.format("The for each source path '%s' is not valid!", foreach.getSourcePath()));
                //String shortSourcePath = foreach.getShortSourcePath();

            } else {
                MapperContext ctx = new MapperContext(rootRuleItem, rootSourceElement);
                Element result = processTransformItem(ctx);
                if (result != null)
                    results.add(result);
            }
            return results;
        }
        throw new RuntimeException("Unsupported input data !");
    }

    private Element processTransformItem(MapperContext ctx) {
        TransformRuleItem currentRuleItem = ctx.getCurrentRuleItem();
        Element currentResultElement = new DataElement(currentRuleItem.getFormat());
        currentResultElement = executeTransformRule(ctx, currentResultElement);
        List<TransformRuleItem> children = currentRuleItem.getChildren();
        if (children != null && !children.isEmpty()) {
            // TODO Need to support for-each
            // If the element is array type, then need to add array item first and set the attribute in it.
            if (currentResultElement.isArray()) {
                TransformForeach currentForeach = currentRuleItem.getTransformForeach();
                if (currentForeach != null && currentForeach.hasSourcePath()) {
                    // Need to do for each on array element to create all array items.
                    // TODO
                    Path foreachSourcePath = new AccessPath(currentForeach.getSourcePath());
                    // TODO need to calculate the path index here if the parent level contains an for each array
                    Element foreachSourceElement = ScriptUtils.getStartElement(ctx);
                    Element forEachSrcElement = foreachSourceElement.getChildByPath(foreachSourcePath.removeLeft(foreachSourceElement.getLevel() + 1));
                    if (forEachSrcElement == null && !forEachSrcElement.hasChildren())
                        return null;
                    List<Element> forEachArray = forEachSrcElement.getChildren();
                    for (int index = currentForeach.getIndexValue(); index < forEachArray.size(); ++index) {
                        Element sourceElement = forEachArray.get(index);
                        ForeachMapperContext foreachCtx = new ForeachMapperContext(ctx, currentForeach, index, sourceElement);
                        // Execute the children script
                        processChildRuleItem(foreachCtx, currentResultElement.addArrayItem(), children);
                    }
                } else {
                    // By default only add one array item
                    currentResultElement = currentResultElement.addArrayItem();
                    // Execute the children script
                    processChildRuleItem(ctx, currentResultElement, children);
                }
            } else {
                // Execute the children script
                processChildRuleItem(ctx, currentResultElement, children);
            }
        }
        return currentResultElement;
    }

    private void processChildRuleItem(MapperContext parentCtx, Element parentResult, List<TransformRuleItem> children) {
        for (TransformRuleItem child : children) {
            Element childResult = processTransformItem(new MapperContext(parentCtx, child));
            if (childResult != null)
                parentResult.addChild(childResult);
        }
    }

    private Element executeTransformRule(MapperContext ctx, Element result) {
        Script script = ctx.getCurrentRuleItem().getScript();
        if (script != null) {
            Object value = script.execute(ctx);
            if (value != null && result.getFormat().getType() != Type.NONE)
                result.setValue(value);
        }
        return result;
    }
}
