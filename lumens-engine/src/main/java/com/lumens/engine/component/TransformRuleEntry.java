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
    private String sourceName;
    private String targetName;
    private String sourceFmtName;
    private String targetFmtName;
    private TransformRule rule;

    public TransformRuleEntry(String name, String sourceName, String sourceFmtName, String targetName, String targetFmtName, TransformRule rule) {
        this.name = name;
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.sourceFmtName = sourceFmtName;
        this.targetFmtName = targetFmtName;
        this.rule = rule;
    }

    public TransformRuleEntry(String sourceName, String sourceFmtName, String targetName, String targetFmtName, TransformRule rule) {
        this(sourceName + '-' + targetName, sourceName, sourceFmtName, targetName, targetFmtName, rule);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public String getSourceFormatName() {
        return this.sourceFmtName;
    }

    public String getTargetName() {
        return this.targetName;
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
