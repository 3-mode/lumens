/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.model.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.lumens.model.Format;
import java.io.Reader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.comment.CommentStartsWith;
import org.supercsv.quote.AlwaysQuoteMode;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class CSVHelper implements TextConstants {

    protected Map<String, Value> options = new HashMap();
    private CellProcessor[] allProcessors;
    private ICsvListReader listReader;
    private ICsvListWriter listWriter;    

    public CSVHelper(Format fmt) {
        Init(fmt);
    }

    private void Init(Format fmt) {
        int count = fmt.getChildren().size();
        allProcessors = new CellProcessor[count];
        while (count-- > 0) {
            allProcessors[count] = null;
        }

        options.put(QUOTE_CHAR, new Value("\""));
        options.put(FILEDELIMITER, new Value(","));
        options.put(LINEDELIMITER, new Value("\r\n"));
        options.put(OPTION_SURROUNDING_SPACES_NEED_QUOTES, new Value(false));
        options.put(OPTION_SKIP_COMMENTS, new Value(false));
        options.put(OPTION_SURROUNDING_SPACES_NEED_QUOTES, new Value(false));
        options.put(OPTION_QUOTE_MODE, new Value(false));
    }

    private CsvPreference CreatePreperence() {
        char quoteChar = options.get(QUOTE_CHAR).getString().charAt(0);
        int delimiterChar = options.get(FILEDELIMITER).getString().charAt(0);
        String endOfLineSymbols = options.get(LINEDELIMITER).getString();
        boolean ignoreEmptyLines = options.get(OPTION_IGNORE_EMPTYLINE).getBoolean();
        boolean skipComments = options.get(OPTION_SKIP_COMMENTS).getBoolean();
        boolean surroundingSpacesNeedQuotes = options.get(OPTION_SURROUNDING_SPACES_NEED_QUOTES).getBoolean();
        boolean alwaysQuoteMode = options.get(OPTION_QUOTE_MODE).getBoolean();

        CsvPreference.Builder builder =  new CsvPreference.Builder(quoteChar, delimiterChar, endOfLineSymbols)
                .ignoreEmptyLines(ignoreEmptyLines)
                .surroundingSpacesNeedQuotes(surroundingSpacesNeedQuotes);
        if (skipComments){
            builder.skipComments(new CommentStartsWith("#"));
        }
        if (alwaysQuoteMode){
            builder.useQuoteMode(new AlwaysQuoteMode());
        }
        
        return builder.build();
    }

    public List<String> Read(Reader reader) throws Exception {
        ICsvListReader listReader = null;

        try {
            listReader = new CsvListReader(reader, CreatePreperence());
            listReader.getHeader(true); // skip the header (can't be used with CsvListReader)           

            List<Object> customerList;
            while ((customerList = listReader.read(allProcessors)) != null) {
                System.out.println(String.format("lineNo=%s, rowNo=%s, customerList=%s", listReader.getLineNumber(),
                        listReader.getRowNumber(), customerList));
            }

        } finally {
            if (listReader != null) {
                listReader.close();
            }
        }

        return null;
    }

    public CSVHelper SetOption(String key, Value v) {
        Value val;
        if ((val = options.get(key)) != null) {
            val.set(v);
        } else {
            options.put(key, v);
        }

        return this;
    }
}
