/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import com.lumens.io.JsonUtility;
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

    @GET
    @Produces("application/json")
    public Response listLogItem(@PathParam("more") boolean more, @PathParam("offset") int offset, @PathParam("size") int size) {
        // TODO need handle offset, more, size
        try {
            long startTime = System.currentTimeMillis();
            String[] strLines = null;
            if (more) {

            } else {
                strLines = discoverLastLogLines();
            }
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
            json.writeArrayFieldStart("logs");
            for (String strLine : strLines) {
                json.writeStartObject();
                json.writeStringField("message", strLine);
                json.writeEndObject();
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

    private String[] discoverLastLogLines() throws FileNotFoundException, IOException {
        String filePath = ApplicationContext.getLogPath();
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            System.out.println("Log file: " + filePath);
            int lines = 0;
            long length = file.length();
            length--;
            file.seek(length);
            long seek = length;
            List<Byte> byteList = new ArrayList<>();
            for (; seek >= 0; --seek) {
                file.seek(seek);
                byte b = file.readByte();
                if ((char) b == '\n' && lines++ == 500) {
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
            String[] strLines = strContent.split("\n");
            return strLines;
        }
    }
}
