/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component.instrument;

import com.lumens.connector.ElementChunk;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.component.AbstractTransformComponent;
import com.lumens.engine.Instrument;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.RuleComponent;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.engine.ExecuteContext;
import com.lumens.engine.TransformException;
import com.lumens.engine.handler.InputOutputInspectionHandler;
import com.lumens.engine.handler.InspectionHandler;
import com.lumens.logsys.SysLogFactory;
import com.lumens.model.Element;
import com.lumens.processor.Processor;
import com.lumens.processor.ProcessorContext;
import com.lumens.processor.ProcessorExecutionContext;
import com.lumens.processor.transform.TransformMapper;
import com.lumens.processor.transform.TransformRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DataTransformer extends AbstractTransformComponent implements RuleComponent, Instrument {
    private final Logger log = SysLogFactory.getLogger(DataTransformer.class);
    private String name;
    private final Processor processor;
    private final List<TransformRuleEntry> ruleList = new ArrayList<>();
    private final Map<String, List<TransformRuleEntry>> ruleFindList = new HashMap<>();

    public DataTransformer(String id) {
        super("type-transformer", id);
        processor = new TransformMapper();
    }

    public DataTransformer(String id, String name) {
        this(id);
        this.name = name;
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
        String findFormatName = (StringUtils.isEmpty(rule.getSourceId())
                                 || StringUtils.isEmpty(rule.getSourceFormatName()))
                                ? rule.getName() : rule.getSourceFormatName();
        List<TransformRuleEntry> rules = ruleFindList.get(findFormatName);
        if (rules == null) {
            rules = new ArrayList<>();
            ruleFindList.put(findFormatName, rules);
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
                String findFormatName = StringUtils.isEmpty(rule.getSourceFormatName()) ? rule.getName() : rule.getSourceFormatName();
                List<TransformRuleEntry> rules = ruleFindList.get(findFormatName);
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
        ElementChunk inputChunk = context.getInput();
        List<TransformRuleEntry> rules = ruleFindList.get(targetFmtName);
        List<ExecuteContext> exList = new ArrayList<>();
        handleInputLogging(context.getInspectionHandlers(), targetFmtName, inputChunk.getData());
        // TODO in current design, only one target rule can be found
        for (TransformRuleEntry rule : rules) {
            List<Element> results = executeTransform(context, rule, inputChunk, exList);
            handleOutputLogging(context.getInspectionHandlers(), rule.getTargetFormatName(), results);
        }
        return exList;
    }

    private List<Element> executeTransform(ExecuteContext context, TransformRuleEntry rule, ElementChunk inputChunk, List<ExecuteContext> exList) {
        try {
            ProcessorContext pCtx = new ProcessorExecutionContext(rule.getRule(), inputChunk.getData());
            List<Element> results = (List<Element>) processor.execute(pCtx);
            // Push the result to target components
            if (!results.isEmpty() && this.hasTarget()) {
                for (TransformComponent target : this.getTargetList().values())
                    if (!results.isEmpty() && target.accept(rule.getTargetFormatName()))
                        exList.add(new TransformExecuteContext(context,
                                                               new ElementChunk(inputChunk.isLast(), results),
                                                               target, rule.getTargetFormatName(),
                                                               context.getInspectionHandlers()));
            }
            return results;
        } catch (Exception e) {
            // TODO If ignore error then continue
            throw new TransformException(this, e);
        }
    }

    private void handleInputLogging(List<InspectionHandler> handlers, String targetFmtName, List<Element> input) {
        if (log.isDebugEnabled())
            log.debug(String.format("Transform '%s', target format is '%s', input chunk size '%d'.", getName(), targetFmtName, input != null ? input.size() : 0));
        for (InspectionHandler handler : handlers)
            if (handler instanceof InputOutputInspectionHandler)
                ((InputOutputInspectionHandler) handler).processInput(this, targetFmtName, input);
    }

    private void handleOutputLogging(List<InspectionHandler> handlers, String targetName, List<Element> results) {
        if (log.isDebugEnabled())
            log.debug(String.format("Transform '%s' result chunk size '%d'.", getName(), results.size()));
        for (InspectionHandler handler : handlers)
            if (handler instanceof InputOutputInspectionHandler)
                ((InputOutputInspectionHandler) handler).processOutput(this, targetName, results);
    }

    @Override
    public void open() {
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

    @Override
    public void start() {
        this.processor.start();
    }

    @Override
    public void stop() {
        this.processor.stop();
    }

}
