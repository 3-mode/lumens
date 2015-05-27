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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerator;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/log")
public class LogService {

    protected static class DiscoverFileLog {
        public long count;
        public String[] messages;

        public DiscoverFileLog() {
            this(0);
        }

        public DiscoverFileLog(long count) {
            this.count = count;
            messages = new String[0];
        }
    }

    @GET
    @Path("/file")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listLogItem(@QueryParam("job_id") long jobID, @QueryParam("count") long count) {
        // TODO need handle offset, more, size
        System.out.println("[" + jobID + ";" + count + ";" + count + "]");
        try {
            long startTime = System.currentTimeMillis();
            DiscoverFileLog fileLog = discoverLastLogLines(new DiscoverFileLog(count), jobID);
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "OK");
            json.writeObjectFieldStart("result_content");
            json.writeArrayFieldStart("messages");
            for (String strLine : fileLog.messages) {
                json.writeString(strLine);
            }
            json.writeEndArray();
            json.writeEndObject();
            json.writeEndObject();
            if ((System.currentTimeMillis() - startTime) < 2000) {
                Thread.sleep(1000);
            }
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
            //return ServerUtils.getErrorMessageResponse(e);
        }
    }

    private DiscoverFileLog discoverLastLogLines(DiscoverFileLog fileLog, long jobID) throws FileNotFoundException, IOException {
        String filePath = null;
        if (jobID == 0) {
            filePath = ApplicationContext.getServerLogPath();
        } else {
            filePath = String.format("%s/logs/lumens-job-%s.log", ApplicationContext.get().getRealPath(), Long.toString(jobID));
        }
        if (new File(filePath).exists()) {
            try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
                System.out.println("Log file: " + filePath + "; offset: " + fileLog.count);
                int lines = 0;
                long seek = file.length();
                if (seek > 0) {
                    --seek;
                    List<Byte> byteList = new ArrayList<>();
                    for (; seek >= 0; --seek) {
                        file.seek(seek);
                        byte b = file.readByte();
                        if ((char) b == '\n' && lines++ == fileLog.count) {
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
