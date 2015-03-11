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
import java.util.Map;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextClient implements TextConstants {

    private final TextConnector connector;
    Map<String, Value> props;

    public TextClient(TextConnector connector, Map<String, Value> props) {
        this.connector = connector;
        this.props = props;
    }

    public List<Element> read(Element elem, Format fmt) {
        List<Element> result = new ArrayList();
        Element param = elem.getChild(FORMAT_PARAMS);
        String path = param.getChild(PATH) == null ? props.get(PATH).getString() : param.getChild(PATH).getValue().toString();
        int maxLine = props.get(OPTION_MAXLINE).getInt();

        // Add file list
        List<File> files = new ArrayList();
        File fileOrDir = new File(path);
        if (!fileOrDir.exists()) {
            throw new RuntimeException("Path not exist!");
        }
        if (fileOrDir.isFile()) {
            files.add(new File(path));
        } else if (fileOrDir.isDirectory()) {
            for (File f : fileOrDir.listFiles(new LocalFileNameFilter(props.get(FILE_FILTER).getString()))) {
                if (f.isFile()) {
                    files.add(f);
                }
            }
        }

        CSVHelper helper = new CSVHelper()
                .setOption(QUOTE_CHAR, new Value(props.get(QUOTE_CHAR).getString()))
                .setOption(ESCAPE_CHAR, new Value(props.get(ESCAPE_CHAR).getString()))
                .setOption(FILEDELIMITER, new Value(props.get(FILEDELIMITER).getString()))
                .setOption(LINEDELIMITER, new Value(props.get(LINEDELIMITER).getString()))
                .setOption(OPTION_IGNORE_EMPTYLINE, new Value(true))
                .setOption(OPTION_SKIP_COMMENTS, new Value(true))
                .setOption(OPTION_TRIM_SPACE, new Value(props.get(OPTION_TRIM_SPACE).getBoolean()))
                .setOption(ENCODING, new Value(props.get(ENCODING).getString()))
                .setOption(OPTION_MAXLINE, new Value(props.get(OPTION_MAXLINE).getInt()));

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), props.get(ENCODING).getString()))) {
                helper.build(reader);

                if (props.get(OPTION_FIRST_LINE_ASTITLE).getBoolean()) {
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
                        if (!props.get(OPTION_IGNORE_READLINE_ERROR).getBoolean()) {
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
        boolean formatAsTitle = props.get(OPTION_FORMAT_ASTITLE).getBoolean();
        String linedelimiter = props.get(LINEDELIMITER).getString();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(props.get(PATH).getString(), append), props.get(ENCODING).getString()))) {
            StringBuilder title = new StringBuilder();
            StringBuilder line = new StringBuilder();
            for (Element field : elem.getChildren()) {
                if (FORMAT_PARAMS.equalsIgnoreCase(field.getFormat().getName())) {
                    continue;
                }

                String fieldString = field.getValue().toString();
                if (props.get(OPTION_TRIM_SPACE).getBoolean()) {
                    fieldString = fieldString.trim();
                }
                if (formatAsTitle && title.length() > 0) {
                    title.append(props.get(FILEDELIMITER).getString());
                }
                if (formatAsTitle) {
                    title.append(field.getFormat().getName());
                }
                if (line.length() > 0) {
                    line.append(props.get(FILEDELIMITER).getString());
                }

                if (props.get(OPTION_QUOTE_MODE).getBoolean()) {
                    line.append(props.get(QUOTE_CHAR).getString()).append(fieldString).append(props.get(QUOTE_CHAR).getString());
                } else {
                    line.append(fieldString);
                }
            }

            if (formatAsTitle) {
                writer.write(title.toString());
                if (linedelimiter.isEmpty()) {
                    writer.newLine();
                } else {
                    writer.write(linedelimiter);
                }
                formatAsTitle = false;
            }

            writer.write(line.toString());

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
