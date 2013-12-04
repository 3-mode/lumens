/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.run.ExecuteContext;
import com.lumens.model.Element;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DataTransformation extends AbstractTransformComponent implements RuleComponent {
    private String name;
    private Processor processor;
    private List<TransformRuleEntry> ruleList = new ArrayList<>();
    private Map<String, List<TransformRuleEntry>> ruleFindList = new HashMap<>();

    public DataTransformation() {
        processor = new TransformProcessor();
    }

    @Override
    public void registerRule(TransformRuleEntry rule) {
        for (TransformRuleEntry r : ruleList)
            if (r.getName().equals(rule.getName()))
                return;
        ruleList.add(rule);
        List<TransformRuleEntry> rules = ruleFindList.get(rule.getSourceName());
        if (rules == null) {
            rules = new ArrayList<>();
            ruleFindList.put(rule.getSourceName(), rules);
        }
        rules.add(rule);
    }

    @Override
    public TransformRuleEntry removeRule(String ruleName) {
        Iterator<TransformRuleEntry> it = ruleList.iterator();
        while (it.hasNext()) {
            TransformRuleEntry rule = it.next();
            if (rule.getName().equals(ruleName)) {
                it.remove();
                List<TransformRuleEntry> rules = ruleFindList.get(rule.getSourceName());
                if (rules == null)
                    throw new RuntimeException("Not found");
                rules.remove(rule);
                return rule;
            }
        }
        return null;
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context) {
        List<Element> results = new ArrayList<>();
        String targetId = context.getTargetName();
        List<TransformRuleEntry> rules = ruleFindList.get(targetId);
        List<ExecuteContext> exList = new ArrayList<>();
        Object input = context.getInput();
        for (TransformRuleEntry rule : rules) {
            if (input != null && input instanceof List) {
                List list = (List) input;
                if (!list.isEmpty() && list.get(0) instanceof Element) {
                    List<Element> inputs = (List<Element>) input;
                    for (Element data : inputs) {
                        List<Element> result = (List<Element>) processor.execute(rule.getRule(), data);
                        if (!result.isEmpty())
                            results.addAll(result);
                    }
                }
            } else if (input == null || input instanceof Element) {
                Element data = input == null ? null : (Element) input;
                List<Element> result = (List<Element>) processor.execute(rule.getRule(), data);
                if (!result.isEmpty())
                    results.addAll(result);
            }
            exList.add(new TransformExecuteContext(results, rule.getTargetName()));
        }
        return exList;
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean accept(ExecuteContext ctx) {
        return ruleFindList.containsKey(ctx.getTargetName());
    }

    @Override
    public boolean isSingleTarget() {
        return true;
    }

    public List<TransformRuleEntry> getTransformRuleList() {
        return ruleList;
    }

    @Override
    public String getClassName() {
        return DataTransformation.class.getName();
    }
}
