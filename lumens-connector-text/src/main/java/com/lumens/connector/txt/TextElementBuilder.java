/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextElementBuilder implements TextConstants {
    public static Element buildElement(Format fmt, List<Object> columns) throws Exception {
        if (fmt == null || fmt.getChildren() == null) {
            return null;
        }

        Element elem = new DataElement(fmt);
        List<Format> children = fmt.getChildren();
        int index = 0;
        for (Format child : children) {
            int size = columns.size();
            if (!FORMAT_PARAMS.equalsIgnoreCase(child.getName())){
                elem.addChild(child.getName()).setValue(new Value(child.getType(), index > size - 1 ? null : columns.get(index++)));
            }
        }

        return elem;
    }
}
