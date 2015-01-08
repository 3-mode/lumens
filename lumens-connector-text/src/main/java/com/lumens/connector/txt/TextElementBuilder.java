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
    public static Element buildElement(Format fmt, List<Object> columns) throws Exception {
        if (fmt == null || fmt.getChildren() == null) {
            return null;
        }
/*
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
                PatternParser parser = new RFC4180Parser();
                parser.SetOption(OPTION_TRIM_SPACE, new Value(trim));
                values.addAll(parser.ParseField(curline));
            } catch (PatternSyntaxException ex) {
                throw ex;
            }
        }
*/
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
