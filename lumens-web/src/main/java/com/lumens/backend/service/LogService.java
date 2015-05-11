/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import com.lumens.backend.ServerUtils;
import com.lumens.io.JsonUtility;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
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
        try {

            List<String> logItems = new ArrayList<>(500);
            if (more) {

            } else {
                String filePath = ApplicationContext.getLogPath();
                RandomAccessFile file = new RandomAccessFile(filePath, "r");
                System.out.println("Log file: " + filePath);
                int lines = 0;
                StringBuilder builder = new StringBuilder();
                long length = file.length();
                length--;
                file.seek(length);
                for (long seek = length; seek >= 0; --seek) {
                    file.seek(seek);
                    char c = (char) file.read();
                    if (c == '\n') {
                        builder = builder.reverse();
                        if (builder.length() > 0)
                            logItems.add(builder.toString());
                        lines++;
                        builder = null;
                        builder = new StringBuilder();
                        if (lines == 500) {
                            break;
                        }
                    } else
                        builder.append(c);
                }
            }
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
            json.writeArrayFieldStart("logs");
            int length = logItems.size();
            while (--length >= 0) {
                json.writeStartObject();
                json.writeStringField("message", logItems.get(length));
                json.writeEndObject();
            }
            json.writeEndArray();
            json.writeEndObject();
            json.writeEndObject();
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (IOException e) {
            return ServerUtils.getErrorMessageResponse(e);
        }
    }
}
