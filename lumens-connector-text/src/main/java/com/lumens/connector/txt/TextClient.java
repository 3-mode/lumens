/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.lumens.io.LocalFileNameFilter;
import com.lumens.model.Format;
import com.lumens.model.Element;
import com.lumens.model.Value;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.File;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextClient implements TextConstants {

    private final Map<String, Value> propList;

    public TextClient(Map<String, Value> props) {
        propList = props;
    }

    public List<Element> read(Element elem, Format fmt) {
        List<Element> result = new ArrayList();
        Element param = elem.getChild(FORMAT_PARAMS);
        String encoding = param.getChild(ENCODING) == null ? propList.get(ENCODING).toString() : param.getChild(ENCODING).getValue().toString();
        String path = param.getChild(PATH) == null ? propList.get(PATH).toString() : param.getChild(PATH).getValue().toString();
        String filter = param.getChild(FILE_FILTER) == null ? propList.get(FILE_FILTER).toString() : param.getChild(FILE_FILTER).getValue().toString();
        String delimiter = param.getChild(FILEDELIMITER) == null ? propList.get(FILEDELIMITER).toString() : param.getChild(FILEDELIMITER).getValue().toString();
        String linedelimiter = param.getChild(LINEDELIMITER) == null ? propList.get(LINEDELIMITER).toString() : param.getChild(LINEDELIMITER).getValue().toString();
        String escape = param.getChild(ESCAPE_CHAR) == null ? propList.get(ESCAPE_CHAR).toString() : param.getChild(ESCAPE_CHAR).getValue().toString();
        String quote = param.getChild(QUOTE_CHAR) == null ? propList.get(QUOTE_CHAR).toString() : param.getChild(QUOTE_CHAR).getValue().toString();
        boolean ignoreEmptyLine = param.getChild(OPTION_IGNORE_EMPTYLINE) == null ? propList.get(OPTION_IGNORE_EMPTYLINE).getBoolean() : param.getChild(OPTION_IGNORE_EMPTYLINE).getValue().getBoolean();
        boolean skipComments = param.getChild(OPTION_SKIP_COMMENTS) == null ? propList.get(OPTION_SKIP_COMMENTS).getBoolean() : param.getChild(OPTION_SKIP_COMMENTS).getValue().getBoolean();
        boolean firstLineAsTitle = param.getChild(OPTION_FIRST_LINE_ASTITLE) == null ? propList.get(OPTION_FIRST_LINE_ASTITLE).getBoolean() : param.getChild(OPTION_FIRST_LINE_ASTITLE).getValue().getBoolean();
        boolean ignoreReadlineError = param.getChild(OPTION_IGNORE_READLINE_ERROR) == null ? propList.get(OPTION_IGNORE_READLINE_ERROR).getBoolean() : param.getChild(OPTION_IGNORE_READLINE_ERROR).getValue().getBoolean();
        boolean bTrim = param.getChild(OPTION_TRIM_SPACE) == null ? propList.get(OPTION_TRIM_SPACE).getBoolean() : param.getChild(OPTION_TRIM_SPACE).getValue().getBoolean();
        int maxLine = param.getChild(OPTION_MAXLINE) == null ? propList.get(OPTION_MAXLINE).getInt() : param.getChild(OPTION_MAXLINE).getValue().getInt();

        if (delimiter.equals(escape)) {
            throw new RuntimeException("Delimiter should not be equal to escape char.");
        }

        // Add file list
        List<File> files = new ArrayList();
        File fileOrDir = new File(path);
        if (!fileOrDir.exists()) {
            throw new RuntimeException("Path not exist!");
        }
        if (fileOrDir.isFile()) {
            files.add(new File(path));
        } else if (fileOrDir.isDirectory()) {
            for (File f : fileOrDir.listFiles(new LocalFileNameFilter(filter))) {
                if (f.isFile()) {
                    files.add(f);
                }
            }
        }

        CSVHelper helper = new CSVHelper()
                .setOption(QUOTE_CHAR, new Value(quote))
                .setOption(FILEDELIMITER, new Value(delimiter))
                .setOption(LINEDELIMITER, new Value(linedelimiter))
                .setOption(OPTION_IGNORE_EMPTYLINE, new Value(ignoreEmptyLine))
                .setOption(OPTION_SKIP_COMMENTS, new Value(skipComments))
                .setOption(OPTION_TRIM_SPACE, new Value(bTrim));

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))) {
                helper.build(reader);
                if (firstLineAsTitle) {
                    // TODO: sent first line to UI, how?
                    List<String> titles = helper.readline();
                    continue;
                }

                List<Object> columns;
                while (true) {
                    try {
                        if ((columns = helper.read()) == null) {
                            break;
                        }
                        if (maxLine > 0 && --maxLine <= 0) {
                            break;
                        }

                        Element build = TextElementBuilder.buildElement(fmt, columns);
                        result.add(build);
                    } catch (Exception ex) {
                        if (!ignoreReadlineError) {
                            helper.close();
                            throw new RuntimeException(ex);
                        }else{
                            // TODO: log here, now use System.out.println();
                            System.err.println("Reading error on reading file:" + file.toString());                           
                            System.err.println("Error message:" + ex.getMessage());
                        }
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return result;
    }

    public void write(Element elem, boolean append) {
        Element param = elem.getChild(FORMAT_PARAMS);
        String encoding = param.getChild(ENCODING) == null ? propList.get(ENCODING).toString() : param.getChild(ENCODING).getValue().toString();
        String path = param.getChild(PATH) == null ? propList.get(PATH).toString() : param.getChild(PATH).getValue().toString();
        String delimiter = param.getChild(FILEDELIMITER) == null ? propList.get(FILEDELIMITER).toString() : param.getChild(FILEDELIMITER).getValue().toString();
        String linedelimiter = param.getChild(LINEDELIMITER) == null ? propList.get(LINEDELIMITER).toString() : param.getChild(LINEDELIMITER).getValue().toString();
        String quote = param.getChild(QUOTE_CHAR) == null ? propList.get(QUOTE_CHAR).toString() : param.getChild(QUOTE_CHAR).getValue().toString();
        boolean formatAsTitle = param.getChild(OPTION_FORMAT_ASTITLE) == null ? propList.get(OPTION_FORMAT_ASTITLE).getBoolean() : param.getChild(OPTION_FORMAT_ASTITLE).getValue().getBoolean();
        boolean bTrim = param.getChild(OPTION_TRIM_SPACE) == null ? propList.get(OPTION_TRIM_SPACE).getBoolean() : param.getChild(OPTION_TRIM_SPACE).getValue().getBoolean();
        boolean alwaysQuoteMode = param.getChild(OPTION_QUOTE_MODE) == null ? propList.get(OPTION_QUOTE_MODE).getBoolean() : param.getChild(OPTION_QUOTE_MODE).getValue().getBoolean();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, append), encoding))) {
            String line = "";
            String title = "";
            for (Element field : elem.getChildren()) {
                if (FORMAT_PARAMS.equalsIgnoreCase(field.getFormat().getName())) {
                    continue;
                }

                String fieldString = field.getValue().toString();
                if (bTrim) {
                    fieldString = fieldString.trim();
                }
                if (formatAsTitle && !title.isEmpty()) {
                    title += delimiter;
                }
                if (formatAsTitle) {
                    title += field.getFormat().getName();
                }
                if (!line.isEmpty()) {
                    line += delimiter;
                }

                if (alwaysQuoteMode) {
                    line += quote + fieldString + quote;
                } else {
                    line += fieldString;
                }
            }

            if (formatAsTitle) {
                writer.write(title);
                if (linedelimiter.isEmpty()) {
                    writer.newLine();
                } else {
                    writer.write(linedelimiter);
                }
                formatAsTitle = false;
            }

            writer.write(line);
            if (linedelimiter.isEmpty()) {
                writer.newLine();
            } else {
                writer.write(linedelimiter);
            }

            writer.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
