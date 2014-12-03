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
            List<TransformForeach> rootForeachList = rootRuleItem.getTransformForeach();
            if (!rootForeachList.isEmpty()) {
                int foreachLevelDepth = rootForeachList.size() - 1;
                checkForeachDepthCount(foreachLevelDepth);
                MapperContext ctx = new MapperContext(rootRuleItem, rootSourceElement);
                processRootForeachList(ctx, results, rootForeachList, 0, foreachLevelDepth);
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

    private void processRootForeachList(MapperContext ctx, List<Element> results, List<TransformForeach> rootForeachList, int foreachLevel, int foreachLevelDepth) {
        TransformRuleItem rootRuleItem = ctx.getCurrentRuleItem();
        TransformForeach rootForeach = rootForeachList.get(foreachLevel);
        if (rootForeach != null && rootForeach.hasSourcePath()) {
            Path foreachSourcePath = new AccessPath(rootForeach.getSourcePath());
            Element foreachSourceElement = ScriptUtils.getStartElement(ctx);
            // Need to do for each on array element to create all array items.
            Element forEachSrcElement = foreachSourceElement.getChildByPath(foreachSourcePath.removeLeft(foreachSourceElement.getLevel() + 1));
            if (!forEachSrcElement.hasChildren())
                return;
            List<Element> forEachArray = forEachSrcElement.getChildren();
            for (int index = rootForeach.getIndexValue(); index < forEachArray.size(); ++index) {
                Element sourceElement = forEachArray.get(index);
                ForeachMapperContext foreachCtx = new ForeachMapperContext(ctx, rootForeach, index, sourceElement);
                if (foreachLevel < foreachLevelDepth) {
                    processRootForeachList(foreachCtx, results, rootForeachList, foreachLevel + 1, foreachLevelDepth);
                } else if (foreachLevel == foreachLevelDepth) {
                    Element result = processTransformItem(foreachCtx);
                    if (result != null)
                        results.add(result);
                }
            }
        }
    }

    private void processForeachList(MapperContext ctx, Element parentResultElement, List<TransformRuleItem> ruleItems,
                                    List<TransformForeach> foreachList, int foreachLevel, int foreachLevelDepth) {
        TransformForeach currentForeach = foreachList.get(foreachLevel);
        Path foreachSourcePath = new AccessPath(currentForeach.getSourcePath());
        Element foreachSourceElement = ScriptUtils.getStartElement(ctx);
        Element forEachSrcElement = foreachSourceElement.getChildByPath(foreachSourcePath.removeLeft(foreachSourceElement.getLevel() + 1));
        if (!forEachSrcElement.hasChildren())
            return;
        List<Element> forEachArray = forEachSrcElement.getChildren();
        for (int index = currentForeach.getIndexValue(); index < forEachArray.size(); ++index) {
            // Need to do for each on array element to create all array items.
            Element sourceElement = forEachArray.get(index);
            ForeachMapperContext foreachCtx = new ForeachMapperContext(ctx, currentForeach, index, sourceElement);
            if (foreachLevel < foreachLevelDepth) {
                processForeachList(foreachCtx, parentResultElement, ruleItems, foreachList, foreachLevel + 1, foreachLevelDepth);
            } else if (foreachLevel == foreachLevelDepth) {
                // Execute the children script, don't execute array item script that already processed by array.
                processChildRuleItem(foreachCtx, parentResultElement.addArrayItem(), ruleItems);
            }
        }
    }

    private Element processTransformItem(MapperContext ctx) {
        TransformRuleItem currentRuleItem = ctx.getCurrentRuleItem();
        Element currentResultElement = new DataElement(currentRuleItem.getFormat());
        currentResultElement = executeTransformRule(ctx, currentResultElement);
        List<TransformRuleItem> childRuleItems = currentRuleItem.getChildren();
        if (childRuleItems != null && !childRuleItems.isEmpty()) {
            // If the element is array type, then need to add array item first and set the attribute in it.
            // If the element is not array, then for each configuration is not correct from business point and data model point
            if (currentResultElement.isArray()) {
                List<TransformForeach> foreachList = currentRuleItem.getTransformForeach();
                if (!foreachList.isEmpty()) {
                    int foreachLevelDepth = foreachList.size() - 1;
                    checkForeachDepthCount(foreachLevelDepth);
                    processForeachList(ctx, currentResultElement, childRuleItems, foreachList, 0, foreachLevelDepth);
                } else {
                    // By default only add one array item
                    currentResultElement = currentResultElement.addArrayItem();
                    // Execute the children script
                    processChildRuleItem(ctx, currentResultElement, childRuleItems);
                }
            } else {
                // Execute the children script
                processChildRuleItem(ctx, currentResultElement, childRuleItems);
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

    private void checkForeachDepthCount(int foreachLevelDepth) {
        if (foreachLevelDepth > 5)
            throw new RuntimeException(String.format("Too many for each loop configurations level '%d'", foreachLevelDepth));
    }
}
