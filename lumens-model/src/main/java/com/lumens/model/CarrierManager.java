/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.model;

import com.lumens.logsys.SysLogFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class CarrierManager {

    private final Logger log = SysLogFactory.getLogger(CarrierManager.class);
    private final Map<String, Object> values = new HashMap<>();

    public void take(CarrierManager mgr) {
        if (mgr == null)
            return;
        Iterator<Entry<String, Object>> it = mgr.iterator();
        while (it.hasNext()) {
            Entry<String, Object> en = it.next();
            Object old = values.get(en.getKey());
            if (old != null)
                log.warn(String.format("Duplicate accessory! New value[%s:%s], Old value[%s:%s].", en.getKey(), en.getValue(), en.getKey(), old.toString()));
            values.put(en.getKey(), en.getValue());
        }
    }

    public Object getValue(String name) {
        return values.get(name);
    }

    public Object setValue(String name, Object value) {
        return values.put(name, value);
    }

    public Object remove(String name) {
        return values.remove(name);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public Iterator<Entry<String, Object>> iterator() {
        return values.entrySet().iterator();
    }
}
