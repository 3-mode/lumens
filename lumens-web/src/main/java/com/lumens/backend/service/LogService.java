/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import com.lumens.io.JsonUtility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/log")
public class LogService {

    protected static class DiscoverFileLog {
        public long offset;
        public String[] messages;

        public DiscoverFileLog() {
            this(0);
        }

        public DiscoverFileLog(long offset) {
            this.offset = offset;
            messages = new String[0];
        }
    }

    @GET
    @Path("/file")
    @Produces("application/json")
    public Response listLogItem(@PathParam("more") boolean more, @PathParam("offset") long offset, @PathParam("size") long size) {
        // TODO need handle offset, more, size
        try {
            long startTime = System.currentTimeMillis();
            DiscoverFileLog fileLog = discoverLastLogLines(new DiscoverFileLog(offset), more);
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
            json.writeNumberField("offset", fileLog.offset);
            json.writeArrayFieldStart("messages");
            for (String strLine : fileLog.messages) {
                json.writeString(strLine);
            }
            json.writeEndArray();
            json.writeEndObject();
            json.writeEndObject();
            if ((System.currentTimeMillis() - startTime) < 3000) {
                Thread.sleep(2000);
            }
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
            //return ServerUtils.getErrorMessageResponse(e);
        }
    }

    private DiscoverFileLog discoverLastLogLines(DiscoverFileLog fileLog, boolean more) throws FileNotFoundException, IOException {
        String filePath = ApplicationContext.getLogPath();
        if (new File(filePath).exists()) {
            try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
                System.out.println("Log file: " + filePath);
                int lines = 0;
                long length = file.length();
                if (length > 0) {
                    length--;
                    if (more && length > fileLog.offset) {
                        length = fileLog.offset;
                    }
                    file.seek(length);
                    long seek = length;
                    List<Byte> byteList = new ArrayList<>();
                    for (; seek >= 0; --seek) {
                        fileLog.offset = seek;
                        file.seek(seek);
                        byte b = file.readByte();
                        if ((char) b == '\n' && lines++ == 100) {
                            break;
                        }
                        byteList.add(b);
                    }
                    Collections.reverse(byteList);
                    byte[] bs = new byte[byteList.size()];
                    int i = 0;
                    for (byte b : byteList)
                        bs[i++] = b;
                    String strContent = new String(bs, "UTF-8");
                    if (strContent == null && strContent.isEmpty())
                        return null;
                    fileLog.messages = strContent.split("\n");
                }
            }
        }
        return fileLog;
    }
}
