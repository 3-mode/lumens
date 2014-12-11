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
import com.lumens.engine.ExecuteContext;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.handler.TransformerResultHandler;
import com.lumens.model.Element;
import com.lumens.processor.Processor;
import com.lumens.processor.transform.TransformMapper;
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
public class DataTransformer extends AbstractTransformComponent implements RuleComponent, Instrument {

    private String name;
    private final Processor processor;
    private final List<TransformRuleEntry> ruleList = new ArrayList<>();
    private final Map<String, List<TransformRuleEntry>> ruleFindList = new HashMap<>();

    public DataTransformer(String id) {
        super("type-transformer", id);
        processor = new TransformMapper();
    }

    @Override
    public TransformRule registerRule(FormatEntry srcFormatEntry, FormatEntry destFormatEntry) {
        if (srcFormatEntry == null)
            return registerRule(new TransformRuleEntry(getId(), destFormatEntry.getName(), destFormatEntry.getDataSourceId(), destFormatEntry.getName(), new TransformRule(destFormatEntry.getFormat())));
        return registerRule(new TransformRuleEntry(srcFormatEntry.getDataSourceId(), srcFormatEntry.getName(), destFormatEntry.getDataSourceId(), destFormatEntry.getName(), new TransformRule(destFormatEntry.getFormat())));
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
        List<ExecuteContext> exList = new ArrayList<>();
        String targetFmtName = context.getTargetFormatName();
        List<TransformRuleEntry> rules = ruleFindList.get(targetFmtName);
        List<Element> input = context.getInput();
        for (TransformRuleEntry rule : rules) {
            List<Element> results = new ArrayList<>();
            List<Element> result = (List<Element>) processor.execute(rule.getRule(), input);
            if (!result.isEmpty())
                results.addAll(result);

            if (!results.isEmpty() && this.hasTarget()) {
                for (TransformComponent target : this.getTargetList().values())
                    if (!result.isEmpty() && target.accept(rule.getTargetFormatName()))
                        exList.add(new TransformExecuteContext(context, results, target, rule.getTargetFormatName(), context.getResultHandlers()));
            }
            for (ResultHandler handler : context.getResultHandlers())
                if (handler instanceof TransformerResultHandler)
                    handler.process(this, targetFmtName, results);
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
    public boolean accept(String name) {
        return ruleFindList.containsKey(name);
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
        //throw new LumensException("DataTransformer only can link to one source");
    }

    @Override
    public void targetTo(TransformComponent target) {
        //if (getTargetList().isEmpty())
        super.targetTo(target);
        //throw new LumensException("DataTransformer only can link to one target");
    }
}
