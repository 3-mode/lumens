/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.processor.transform.TransformRule;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface RuleComponent {

    public TransformRule registerRule(FormatEntry srcFormatEntry, FormatEntry destFormatEntry);

    public TransformRule registerRule(TransformRuleEntry rule);

    public TransformRuleEntry removeRule(String ruleName);
}
