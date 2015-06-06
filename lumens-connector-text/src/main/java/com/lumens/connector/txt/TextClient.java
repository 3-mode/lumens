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
import java.io.File;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextClient implements TextConstants {

    private final TextConnector connector;

    public TextClient(TextConnector connector) {
        this.connector = connector;
    }

    public List<Element> read(Element elem, Format fmt) {
        List<Element> result = new ArrayList();
        Element param = elem.getChild(FORMAT_PARAMS);
        String path = param.getChild(PATH) == null ? connector.getPath() : param.getChild(PATH).getValue().toString();
        int maxLine = connector.getMaxLine();

        // Add file list
        List<File> files = new ArrayList();
        File fileOrDir = new File(path);
        if (!fileOrDir.exists()) {
            throw new RuntimeException("Path not exist!");
        }
        if (fileOrDir.isFile()) {
            files.add(new File(path));
        } else if (fileOrDir.isDirectory()) {
            for (File f : fileOrDir.listFiles(new LocalFileNameFilter(connector.getFilter()))) {
                if (f.isFile()) {
                    files.add(f);
                }
            }
        }

        CSVHelper helper = new CSVHelper()
        .setOption(ESCAPE_CHAR, new Value(connector.getEncoding()))
        .setOption(QUOTE_CHAR, new Value(connector.getQuote()))
        .setOption(FIELDELIMITER, new Value(connector.getDelimiter()))
        .setOption(LINEDELIMITER, new Value(connector.getLinedelimiter()))
        .setOption(OPTION_IGNORE_EMPTYLINE, new Value(connector.isIgnoreEmptyLine()))
        .setOption(OPTION_SKIP_COMMENTS, new Value(connector.isSkipComments()))
        .setOption(OPTION_TRIM_SPACE, new Value(connector.isTrim()))
        .setOption(OPTION_MAXLINE, new Value(connector.getMaxLine()));

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), connector.getEncoding()))) {
                helper.build(reader);

                if (connector.isFirstAsTitle()) {
                    // TODO: sent first line to UI, how?
                    List<String> titles = helper.readline();
                    continue;
                }

                List<Object> columns;
                while (true) {
                    try {
                        if ((columns = helper.read()) == null)
                            break;
                        if (maxLine > 0 && --maxLine <= 0)
                            break;
                        Element build = TextElementBuilder.buildElement(fmt, columns);
                        result.add(build);
                    } catch (Exception ex) {
                        if (!connector.isIgnoreReadlineError()) {
                            helper.close();
                            throw new RuntimeException(ex);
                        } else {
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
        boolean formatAsTitle = connector.isFormatAsTitle();
        String linedelimiter = connector.getLinedelimiter();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(connector.getPath(), append), connector.getEncoding()))) {
            StringBuilder title = new StringBuilder();
            StringBuilder line = new StringBuilder();
            for (Element field : elem.getChildren()) {
                if (FORMAT_PARAMS.equalsIgnoreCase(field.getFormat().getName())) {
                    continue;
                }

                String fieldString = field.getValue().toString();
                if (connector.isTrim()) {
                    fieldString = fieldString.trim();
                }
                if (formatAsTitle && title.length() > 0) {
                    title.append(connector.getDelimiter());
                }
                if (formatAsTitle) {
                    title.append(field.getFormat().getName());
                }
                if (line.length() > 0) {
                    line.append(connector.getDelimiter());
                }

                if (connector.isQuoteMode()) {
                    line.append(connector.getQuote()).append(fieldString).append(connector.getQuote());
                } else {
                    line.append(fieldString);
                }
            }

            if (formatAsTitle) {
                writer.write(title.toString());
                if (linedelimiter.isEmpty())
                    writer.newLine();
                else
                    writer.write(linedelimiter);
                formatAsTitle = false;
            }

            writer.write(line.toString());

            if (linedelimiter == null || linedelimiter.isEmpty())
                writer.newLine();
            else
                writer.write(linedelimiter);

            writer.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
