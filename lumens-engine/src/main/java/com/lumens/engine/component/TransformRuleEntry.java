/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.processor.transform.TransformRule;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformRuleEntry {

    private String name;
    private String sourceId;
    private String targetId;
    private String sourceFmtName;
    private String targetFmtName;
    private TransformRule rule;

    public TransformRuleEntry(String name, String sourceId, String sourceFmtName, String targetId, String targetFmtName, TransformRule rule) {
        this.name = name;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.sourceFmtName = sourceFmtName;
        this.targetFmtName = targetFmtName;
        this.rule = rule;
    }

    public TransformRuleEntry(String sourceId, String sourceFmtName, String targetId, String targetFmtName, TransformRule rule) {
        this(String.format("%s:%s--->%s:%s", sourceId, sourceFmtName, targetId, targetFmtName), sourceId, sourceFmtName, targetId, targetFmtName, rule);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public String getSourceFormatName() {
        return this.sourceFmtName;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public String getTargetFormatName() {
        return this.targetFmtName;
    }

    public TransformRule getRule() {
        return rule;
    }

    public void setRule(TransformRule rule) {
        this.rule = rule;
    }
}
