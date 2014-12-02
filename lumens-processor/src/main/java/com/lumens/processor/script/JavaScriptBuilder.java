/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.processor.script;

import java.io.LineNumberReader;
import java.io.StringReader;

public class JavaScriptBuilder {

    private final String functionPrefix = "function fLumensScript_";
    private final String pathEnding = "+-*/ &|!<>\n\r\t^%=;:?,";

    public String build(String script) {
        // TODO prepare the script and convert it to javascript function
        StringBuilder function = new StringBuilder();
        Long l = System.currentTimeMillis();
        function.append(functionPrefix).append(l).append("(ctx) {\n");
        function.append(replaceAccessPathScriptToFunction(script));
        function.append('}');
        return function.toString();
    }

    private String replaceAccessPathScriptToFunction(String script) {
        try {
            String line = null;
            LineNumberReader reader = new LineNumberReader(new StringReader(script));
            StringBuilder scriptWithoutComments = new StringBuilder();
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("//"))
                    continue;
                line = addElementAccessor(line);
                scriptWithoutComments.append(line).append('\n');
                ++lineCount;
            }
            String scriptString = scriptWithoutComments.toString().trim();
            if (1 == lineCount && !scriptString.startsWith("return"))
                scriptString = scriptWithoutComments.insert(0, "return ").toString();
            return scriptString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String addElementAccessor(String line) {
        StringBuilder builder = new StringBuilder();
        boolean bQuote = false;
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            if (c == '\"')
                bQuote = !bQuote;
            if (bQuote || c != '@') {
                builder.append(c);
            } else if (c == '@') { // find all @a.b.c format line in this line
                i++;
                if (i < line.length()) {
                    boolean singleQuote = false;
                    StringBuilder path = new StringBuilder();
                    while (i < line.length()) {
                        c = line.charAt(i);
                        if (c == '\'')
                            singleQuote = !singleQuote;

                        if (!singleQuote && pathEnding.indexOf(c) > -1) {
                            builder.append("getElementValue(ctx, \"").append(path).append("\")").
                            append(c);
                            break;
                        } else {
                            path.append(c);
                            ++i;
                            if (i == line.length())
                                builder.append("getElementValue(ctx, \"").append(path).append("\")");
                        }
                    }
                }
            }
        }

        return builder.toString();
    }
}
