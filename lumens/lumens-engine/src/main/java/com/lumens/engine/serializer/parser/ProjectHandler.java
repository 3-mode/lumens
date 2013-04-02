/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.serializer.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public interface ProjectHandler
{
    /**
     *
     * A container element start event handling method.
     *
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
     * An empty element event handling method.
     *
     * @param data value or null
     */
    public void handle_position(final Attributes meta) throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_format_entry(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_format_entry() throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_transform_rule_entry(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_transform_rule_entry() throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_target_list(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_target_list() throws SAXException;

    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_property(final String data, final Attributes meta) throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
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
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_format(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_format() throws SAXException;

    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_script(final String data, final Attributes meta) throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_processor_list(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_processor_list() throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_property_list(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_property_list() throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_format_list(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_format_list() throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
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
     *
     * @param meta attributes
     */
    public void start_project(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_project() throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_datasource_list(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_datasource_list() throws SAXException;

    /**
     *
     * A data element event handling method.
     *
     * @param data value or null
     * @param meta attributes
     */
    public void handle_description(final String data, final Attributes meta) throws SAXException;

    /**
     *
     * An empty element event handling method.
     *
     * @param data value or null
     */
    public void handle_target(final Attributes meta) throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_processor(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_processor() throws SAXException;

    /**
     *
     * A container element start event handling method.
     *
     * @param meta attributes
     */
    public void start_datasource(final Attributes meta) throws SAXException;

    /**
     *
     * A container element end event handling method.
     */
    public void end_datasource() throws SAXException;
}
