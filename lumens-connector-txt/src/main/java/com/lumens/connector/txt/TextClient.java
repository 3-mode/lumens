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

import com.lumens.model.Format;
import com.lumens.model.Element;
import java.util.List;
import java.util.ArrayList;



/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextClient {
    String filepath;
    BufferedReader reader;
    BufferedWriter writer;
    boolean bInit = false;

    public TextClient() {
    }

    public List<Element> read(Element elem) {
        List<Element> result = new ArrayList();
        Format fmt = elem.getFormat();
        String encoding = elem.getChild(TextConstants.ENCODING).getValue().toString();
        String path = elem.getChild(TextConstants.PATH).getValue().toString();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                Element build = TextElementBuilder.buildElement(fmt, line);
                result.add(build);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    public void write(Element elem, boolean append) {
        try {
            String encoding = elem.getChild(TextConstants.ENCODING).toString();
            String path = elem.getChild(TextConstants.PATH).toString();
            String delimiter = elem.getChild(TextConstants.FILEDELIMITER).toString();
            String linedelimter = elem.getChild(TextConstants.LINEDELIMITER).toString();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, append), encoding));
            for (Element child : elem.getChildren()) {
                Element fields;
                if (elem.getChild(TextConstants.FORMAT_FIELDS) != null) {
                    fields = child.getChild(TextConstants.FORMAT_FIELDS);
                } else {
                    fields = child;
                }

                String line = "";
                for (Element field : fields.getChildren()) {
                    line = delimiter + field.getValue().toString();
                }
                writer.write(line);
                if (linedelimter.isEmpty())
                    writer.newLine();
                else
                    writer.write(linedelimter);
            }

            writer.flush();
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
