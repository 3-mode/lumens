/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface RuleComponent {
    public void registerRule(TransformRuleEntry rule);

    public TransformRuleEntry removeRule(String ruleName);
}
