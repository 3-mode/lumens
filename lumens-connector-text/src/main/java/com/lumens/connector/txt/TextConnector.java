/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.Connector;
import com.lumens.connector.FormatBuilder;
import com.lumens.connector.Operation;
import com.lumens.connector.Direction;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.Map;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextConnector implements Connector, TextConstants {
    private FormatBuilder formatBuilder;
    private TextClient textClient;
    private boolean isOpen = false;
    private final boolean ignoreEmptyLine = true;
    private final boolean skipComments = true;
    private boolean bTrim = true;
    private boolean ignoreReadlineError = true;
    private String encoding;
    private String path;
    private String filter;
    private String delimiter;
    private String linedelimiter;
    private String escape;
    private String quote;
    private String schemaPath;
    private int maxLine;
    private boolean formatAsTitle;
    private boolean quoteMode;
    private boolean firstAsTitle;

    public boolean isFirstAsTitle() {
        return firstAsTitle;
    }

    public boolean isQuoteMode() {
        return quoteMode;
    }

    public boolean isFormatAsTitle() {
        return formatAsTitle;
    }

    public int getMaxLine() {
        return maxLine;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public boolean isIgnoreEmptyLine() {
        return ignoreEmptyLine;
    }

    public boolean isSkipComments() {
        return skipComments;
    }

    public boolean isTrim() {
        return bTrim;
    }

    public boolean isIgnoreReadlineError() {
        return ignoreReadlineError;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getPath() {
        return path;
    }

    public String getFilter() {
        return filter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getLinedelimiter() {
        return linedelimiter;
    }

    public String getEscape() {
        return escape;
    }

    public String getQuote() {
        return quote;
    }

    @Override
    public void setPropertyList(Map<String, Value> props) {
        if (props.containsKey(SCHEMA_PATH))
            schemaPath = props.get(SCHEMA_PATH).getString();
        if (props.containsKey(ENCODING))
            encoding = props.get(ENCODING).getString();
        if (props.containsKey(PATH))
            path = props.get(PATH).getString();
        if (props.containsKey(FILE_FILTER))
            filter = props.get(FILE_FILTER).getString();
        if (props.containsKey(FIELDELIMITER))
            delimiter = props.get(FIELDELIMITER).getString();
        if (props.containsKey(LINEDELIMITER))
            linedelimiter = props.get(LINEDELIMITER).getString();
        if (props.containsKey(ESCAPE_CHAR))
            escape = props.get(ESCAPE_CHAR).getString();
        if (props.containsKey(QUOTE_CHAR))
            quote = props.get(QUOTE_CHAR).getString();
        if (props.containsKey(OPTION_IGNORE_READLINE_ERROR))
            ignoreReadlineError = props.get(OPTION_IGNORE_READLINE_ERROR).getBoolean();
        if (props.containsKey(OPTION_TRIM_SPACE))
            bTrim = props.get(OPTION_TRIM_SPACE).getBoolean();
        if (props.containsKey(OPTION_MAXLINE))
            maxLine = props.get(OPTION_MAXLINE).getInt();
        if (props.containsKey(OPTION_FIRST_LINE_ASTITLE))
            firstAsTitle = props.get(OPTION_FIRST_LINE_ASTITLE).getBoolean();
        if (props.containsKey(OPTION_FORMAT_ASTITLE))
            formatAsTitle = props.get(OPTION_FORMAT_ASTITLE).getBoolean();
        if (props.containsKey(OPTION_QUOTE_MODE))
            quoteMode = props.get(OPTION_QUOTE_MODE).getBoolean();

        if (delimiter != null && delimiter.equalsIgnoreCase(escape))
            throw new RuntimeException("Delimiter should not be equal to escape char.");
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    // To craete specific client in type of DOM or SAX 
    @Override
    public void open() {
        if (textClient == null) {
            textClient = new TextClient(this);
            formatBuilder = new TextFormatBuilder(getSchemaPath());
            try {
                formatBuilder.initalize();
                isOpen = true;
            } catch (RuntimeException ex) {
                throw ex;
            }
        }
    }

    @Override
    public void close() {
        textClient = null;
        formatBuilder = null;
        isOpen = false;
    }

    @Override
    public Operation getOperation() {
        return new TextOperation(textClient);
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        return formatBuilder.getFormatList(direction);
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        return formatBuilder.getFormat(format, path, direction);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
