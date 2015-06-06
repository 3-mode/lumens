/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.model.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.comment.CommentStartsWith;
import org.supercsv.quote.AlwaysQuoteMode;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class CSVHelper implements TextConstants {

    protected Map<String, Value> options = new HashMap();
    private ICsvListReader listReader;
    private ICsvListWriter listWriter;

    public CSVHelper() {
        initOption();
    }

    public CSVHelper build(Reader reader) throws Exception {
        try {
            if (listReader != null) {
                listReader.close();
            }

            listReader = new CsvListReader(reader, createPreperence());
        } catch (Exception ex) {
            throw new IOException(ex);
        }
        return this;
    }

    public CSVHelper build(Writer writer) throws Exception {
        try {
            if (listWriter != null) {
                listWriter.close();
            }

            listWriter = new CsvListWriter(writer, createPreperence());
        } catch (Exception ex) {
            throw new IOException(ex);
        }
        return this;
    }

    public List<String> readline() throws Exception {
        return listReader.read();
    }

    public int getRowNumber() {
        return listReader.getRowNumber();
    }

    public int getLineNumber() {
        return listReader.getLineNumber();
    }

    // Outside caller is responsible for closing stream
    public List<Object> read() throws Exception {
        List<Object> customerList = null;
        try {
            if (listReader.read() != null) {
                CellProcessor[] processors = new CellProcessor[listReader.length()];
                if (options.get(OPTION_TRIM_SPACE).getBoolean()) {
                    for (int i = 0; i < processors.length; i++) {
                        processors[i] = new Trim();
                    }
                }
                customerList = listReader.executeProcessors(processors);
            }

            if (customerList == null) {
                listReader.close();
                listReader = null;
            }
        } catch (Exception ex) {
            // Not close let outside caller to deal with and to continue or not
            throw ex;
        }

        return customerList;
    }

    public void writeLine(String line) throws Exception {
        try {
            listWriter.write(line);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void write(List<Object> columns) throws Exception {
        try {
            if (columns != null) {
                CellProcessor[] processors = new CellProcessor[columns.size()];
                if (options.get(OPTION_TRIM_SPACE).getBoolean()) {
                    for (int i = 0; i < processors.length; i++) {
                        processors[i] = new Trim();
                    }
                }
                listWriter.write(columns, processors);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void close() throws Exception {
        try {
            if (listReader != null) {
                listReader.close();
            }
            if (listWriter != null) {
                listWriter.close();
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public CSVHelper setOption(String key, Value v) {
        options.put(key, v);

        return this;
    }

    // Private methods
    private void initOption() {
        options.put(QUOTE_CHAR, new Value("\""));
        options.put(FIELDELIMITER, new Value(","));
        options.put(LINEDELIMITER, new Value("\\r\\n"));
        options.put(OPTION_IGNORE_EMPTYLINE, new Value(false));
        options.put(OPTION_SKIP_COMMENTS, new Value(false));
        options.put(OPTION_SURROUNDING_SPACES_NEED_QUOTES, new Value(false));
        options.put(OPTION_QUOTE_MODE, new Value(false));
        options.put(OPTION_TRIM_SPACE, new Value(false));
    }

    private CsvPreference createPreperence() {
        char quoteChar = options.get(QUOTE_CHAR).getString().charAt(0);
        char delimiterChar = options.get(FIELDELIMITER).getString().charAt(0);
        String endOfLineSymbols = options.get(LINEDELIMITER).getString();
        boolean ignoreEmptyLines = options.get(OPTION_IGNORE_EMPTYLINE).getBoolean();
        boolean skipComments = options.get(OPTION_SKIP_COMMENTS).getBoolean();
        boolean surroundingSpacesNeedQuotes = options.get(OPTION_SURROUNDING_SPACES_NEED_QUOTES).getBoolean();
        boolean alwaysQuoteMode = options.get(OPTION_QUOTE_MODE).getBoolean();

        CsvPreference.Builder builder = new CsvPreference.Builder(quoteChar, delimiterChar, endOfLineSymbols)
                .ignoreEmptyLines(ignoreEmptyLines)
                .surroundingSpacesNeedQuotes(surroundingSpacesNeedQuotes);

        if (skipComments) {
            builder.skipComments(new CommentStartsWith("#"));
        }
        if (alwaysQuoteMode) {
            builder.useQuoteMode(new AlwaysQuoteMode());
        }

        return builder.build();
    }
}
