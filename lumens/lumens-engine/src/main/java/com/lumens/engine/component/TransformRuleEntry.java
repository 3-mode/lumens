/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.processor.transform.TransformRule;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformRuleEntry
{
    private String name;
    private String sourceName;
    private String targetSource;
    private TransformRule rule;

    public TransformRuleEntry(String name, String sourceName, String targetName, TransformRule rule)
    {
        this.name = name;
        this.sourceName = sourceName;
        this.targetSource = targetName;
        this.rule = rule;
    }

    public TransformRuleEntry(String sourceName, String targetName, TransformRule rule)
    {
        this(sourceName + '-' + targetName, sourceName, targetName, rule);
    }

    public TransformRuleEntry(String name, String sourceName, String targetName)
    {
        this(name, sourceName, targetName, null);
    }

    public TransformRuleEntry(String sourceName, String targetName)
    {
        this(sourceName + '-' + targetName, sourceName, targetName, null);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSourceName()
    {
        return this.sourceName;
    }

    public String getTargetName()
    {
        return this.targetSource;
    }

    public TransformRule getRule()
    {
        return rule;
    }

    public void setRule(TransformRule rule)
    {
        this.rule = rule;
    }
}
