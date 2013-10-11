package com.hrcms.server.model;

import com.hrcms.server.dao.factory.Column;
import java.util.HashMap;
import java.util.Map;

public class DictItem {
    @Column(name = "HRCMS_DICT_DYNC_COLUMN")
    public Map<String, Object> fields = new HashMap<String, Object>();
}
