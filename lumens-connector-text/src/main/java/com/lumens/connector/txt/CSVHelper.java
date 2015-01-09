/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.model.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.Reader;
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

    public List<String> readline() throws Exception {        
        return listReader.read();
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
            if (listReader != null) {
                listReader.close();
            }
            throw ex;
        }

        return customerList;
    }

    public CSVHelper setOption(String key, Value v) {
        options.put(key, v);

        return this;
    }

    // Private methods
    private void initOption() {
        options.put(QUOTE_CHAR, new Value("\""));
        options.put(FILEDELIMITER, new Value(","));
        options.put(LINEDELIMITER, new Value("\\r\\n"));
        options.put(OPTION_IGNORE_EMPTYLINE, new Value(false));
        options.put(OPTION_SKIP_COMMENTS, new Value(false));
        options.put(OPTION_SURROUNDING_SPACES_NEED_QUOTES, new Value(false));
        options.put(OPTION_QUOTE_MODE, new Value(false));
        options.put(OPTION_TRIM_SPACE, new Value(false));
    }

    private CsvPreference createPreperence() {
        char quoteChar = options.get(QUOTE_CHAR).getString().charAt(0);
        char delimiterChar = options.get(FILEDELIMITER).getString().charAt(0);
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
