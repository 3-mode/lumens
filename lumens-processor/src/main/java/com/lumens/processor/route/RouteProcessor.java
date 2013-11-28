/*
 * (C) Copyright Hewlett-Packard Development Company, L.P. All Rights Reserved.
 */
package com.lumens.processor.route;

import com.lumens.model.Element;
import com.lumens.processor.AbstractProcessor;
import com.lumens.processor.Processor;
import com.lumens.processor.Rule;

public class RouteProcessor extends AbstractProcessor {
    public void addRoutePoint(Processor point, RouteRule rule) {
    }

    @Override
    public Object execute(Rule rule, Element element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
