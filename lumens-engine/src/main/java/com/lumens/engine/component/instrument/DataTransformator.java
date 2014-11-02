/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component.instrument;

import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.component.AbstractTransformComponent;
import com.lumens.engine.Instrument;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.RuleComponent;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.engine.run.ExecuteContext;
import com.lumens.engine.run.LastResultHandler;
import com.lumens.engine.run.ResultHandler;
import com.lumens.model.Element;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformProcessor;
import com.lumens.processor.transform.TransformRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DataTransformator extends AbstractTransformComponent implements RuleComponent, Instrument {

    private String name;
    private Processor processor;
    private List<TransformRuleEntry> ruleList = new ArrayList<>();
    private Map<String, List<TransformRuleEntry>> ruleFindList = new HashMap<>();

    public DataTransformator() {
        super("type-transformator", "0");
        processor = new TransformProcessor();
    }

    @Override
    public TransformRule registerRule(FormatEntry srcFormatEntry, FormatEntry destFormatEntry) {
        if (srcFormatEntry == null)
            return registerRule(new TransformRuleEntry(getName(), destFormatEntry.getName(), destFormatEntry.getDataSourceName(), destFormatEntry.getName(), new TransformRule(destFormatEntry.getFormat())));
        return registerRule(new TransformRuleEntry(srcFormatEntry.getDataSourceName(), srcFormatEntry.getName(), destFormatEntry.getDataSourceName(), destFormatEntry.getName(), new TransformRule(destFormatEntry.getFormat())));
    }

    @Override
    public TransformRule registerRule(TransformRuleEntry rule) {
        for (TransformRuleEntry r : ruleList)
            if (r.getName().equals(rule.getName()))
                return r.getRule();
        ruleList.add(rule);
        List<TransformRuleEntry> rules = ruleFindList.get(rule.getSourceFormatName());
        if (rules == null) {
            rules = new ArrayList<>();
            ruleFindList.put(rule.getSourceFormatName(), rules);
        }
        rules.add(rule);
        return rule.getRule();
    }

    @Override
    public TransformRuleEntry removeRule(String ruleName) {
        Iterator<TransformRuleEntry> it = ruleList.iterator();
        while (it.hasNext()) {
            TransformRuleEntry rule = it.next();
            if (rule.getName().equals(ruleName)) {
                it.remove();
                List<TransformRuleEntry> rules = ruleFindList.get(rule.getSourceFormatName());
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
        String targetFmtName = context.getTargetFormatName();
        List<TransformRuleEntry> rules = ruleFindList.get(targetFmtName);
        List<ExecuteContext> exList = new ArrayList<>();
        Object input = context.getInput();
        for (TransformRuleEntry rule : rules) {
            List<Element> results = new ArrayList<>();
            if (input != null && input instanceof List) {
                List<Element> inputs = (List<Element>) input;
                for (Element data : inputs) {
                    List<Element> result = (List<Element>) processor.execute(rule.getRule(), data);
                    if (!result.isEmpty())
                        results.addAll(result);
                }
            } else if (input == null || input instanceof Element) {
                Element data = input == null ? null : (Element) input;
                List<Element> result = (List<Element>) processor.execute(rule.getRule(), data);
                if (!result.isEmpty())
                    results.addAll(result);
            }
            for (ResultHandler handler : context.getResultHandlers())
                if (!(handler instanceof LastResultHandler))
                    handler.process(this, targetFmtName, results);
            exList.add(new TransformExecuteContext(results, rule.getTargetFormatName(), context.getResultHandlers()));
        }
        return exList;
    }

    @Override
    public void open() throws Exception {
        isOpen = true;
    }

    @Override
    public void close() {
        isOpen = false;
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
        return ruleFindList.containsKey(ctx.getTargetFormatName());
    }

    @Override
    public boolean isSingleTarget() {
        return true;
    }

    public List<TransformRuleEntry> getTransformRuleList() {
        return ruleList;
    }

    @Override
    public void sourceFrom(TransformComponent source) {
        //if (getSourceList().isEmpty())
        super.sourceFrom(source);
        //throw new LumensException("DataTransformator only can link to one source");
    }

    @Override
    public void targetTo(TransformComponent target) {
        //if (getTargetList().isEmpty())
        super.targetTo(target);
        //throw new LumensException("DataTransformator only can link to one target");
    }
}
