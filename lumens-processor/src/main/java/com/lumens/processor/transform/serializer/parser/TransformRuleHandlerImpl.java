/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform.serializer.parser;

import com.lumens.model.Format;
import com.lumens.processor.transform.TransformRule;
import com.lumens.processor.transform.TransformRuleItem;
import java.util.LinkedList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformRuleHandlerImpl implements TransformRuleHandler {

    private Format format;
    private LinkedList<TransformRuleItem> ruleItemStack = new LinkedList<>();
    private TransformRule rule;
    private TransformRuleItem currentRuleItem;
    private List<TransformRule> ruleList;

    public TransformRuleHandlerImpl(Format format, List<TransformRule> ruleList) {
        this.format = format;
        this.ruleList = ruleList;
    }

    @Override
    public void start_transform_rule_list(final Attributes meta) throws SAXException {
    }

    @Override
    public void end_transform_rule_list() throws SAXException {
    }

    @Override
    public void start_transform_rule(final Attributes meta) throws SAXException {
        if (format != null) {
            String name = meta.getValue("name");
            if (name != null && format.getName().equals(name)) {
                rule = new TransformRule(format);
                ruleList.add(rule);
            }
        }
    }

    @Override
    public void end_transform_rule() throws SAXException {
    }

    @Override
    public void start_transform_rule_item(final Attributes meta) throws SAXException {
        if (currentRuleItem == null) {
            currentRuleItem = rule.getRootRuleItem();
        } else {
            String name = meta.getValue("format-name");
            if (name != null) {
                TransformRuleItem child = currentRuleItem.addChild(name);
                if (child != null) {
                    String forEachPath = meta.getValue("for-each-path");
                    if (forEachPath != null)
                        child.setForEachPath(forEachPath);
                    ruleItemStack.add(currentRuleItem);
                    currentRuleItem = child;
                }
            }
        }
    }

    @Override
    public void end_transform_rule_item() throws SAXException {
        if (!ruleItemStack.isEmpty())
            currentRuleItem = ruleItemStack.removeLast();
    }

    @Override
    public void handle_script(final String data, final Attributes meta) throws SAXException {
        if (currentRuleItem != null && data != null) {
            try {
                currentRuleItem.setScript(data.trim());
            } catch (Exception ex) {
                throw new SAXException(ex);
            }
        }
    }
}
