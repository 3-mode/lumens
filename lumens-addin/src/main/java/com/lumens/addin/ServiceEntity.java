/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import java.util.Map;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 * @param <T>
 */
public class ServiceEntity<T> {

    private final T service;
    private final Map<String, Object> props;

    public ServiceEntity(T service, Map<String, Object> props) {
        this.service = service;
        this.props = props;
    }

    public T getService() {
        return service;
    }

    public Map<String, Object> getPropertList() {
        return props;
    }
}
