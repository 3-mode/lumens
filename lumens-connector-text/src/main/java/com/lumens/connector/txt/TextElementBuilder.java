/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextElementBuilder implements TextConstants {
    public static Element buildElement(Format fmt, String line, String delimiter, String escape, String quote) throws Exception {
        if (fmt == null || fmt.getChildren() == null || line == null || delimiter == null) {
            return null;
        }

        boolean bNormalEscape = (escape != null) && escape.equals("\"")
                                && (quote != null) && (quote.equals("\""))
                                && (delimiter.equals(","));

        List<String> values = new ArrayList();

        // Deal with first element empty
        int start = 0;
        while (line.charAt(start) == delimiter.charAt(0)) {
            values.add("");
            start++;
        }
        String curline = start > 0 ? line.substring(start) : line;

        if (!bNormalEscape) {
            // TODO: support specific escape char quote char. Need to build customize pattern  
        } else {
            try {
                values.addAll((new RFC4180Parser()).ParseField(curline));
            } catch (PatternSyntaxException ex) {
                throw ex;
            }
        }

        Element elem = new DataElement(fmt);

        List<Format> children = fmt.getChildren();
        int index = 0;
        for (Format child : children) {
            int size = values.size();
            if (!FORMAT_PARAMS.equalsIgnoreCase(child.getName()))
                elem.addChild(child.getName()).setValue(new Value(child.getType(), index > size - 1 ? null : values.get(index++)));
        }

        return elem;
    }
}
