/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.transform.serializer.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface TransformRuleHandler
{

    /**
     *
     * A container element start event handling method.
     * @param meta attributes
     */
    public void start_transform_rule_item(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_transform_rule_item() throws SAXException;

    /**
     *
     * A container element start event handling method.
     * @param meta attributes
     */
    public void start_transform_rule_list(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_transform_rule_list() throws SAXException;

    /**
     *
     * A container element start event handling method.
     * @param meta attributes
     */
    public void start_transform_rule(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_transform_rule() throws SAXException;

    /**
     *
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     */
    public void handle_script(final String data, final Attributes meta) throws SAXException;
    
}
