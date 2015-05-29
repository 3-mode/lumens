/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend;

import com.lumens.io.JsonUtility;
import com.lumens.logsys.SysLogFactory;
import com.lumens.model.DateTime;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;

public class ServerUtils {
    private static final Logger log = SysLogFactory.getLogger(ServerUtils.class);

    public static Timestamp getTimestampFromString(String date) {
        try {
            return new Timestamp(DateTime.DATETIME_PATTERN[4].parse(date).getTime());
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getJobStartTimeString(Timestamp ts) {
        return DateTime.DATETIME_PATTERN[4].format(new Date(ts.getTime()));
    }

    public static String getNormalizedPath(String path) {
        return Paths.get(new File(path).toURI()).normalize().toFile().getAbsolutePath();
    }

    public static Response getErrorMessageResponse(String error) {
        try {
            JsonUtility utility = JsonUtility.createJsonUtility();
            JsonGenerator json = utility.getGenerator();
            json.writeStartObject();
            json.writeStringField("status", "Failed");
            json.writeStringField("error_message", error);
            json.writeEndObject();
            // TODO
            System.err.println(utility.toUTF8String());
            return Response.ok().entity(utility.toUTF8String()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Response getErrorMessageResponse(Exception ex) {
        System.err.print(ex);
        log.error(ex);
        return getErrorMessageResponse(ex.toString());
    }
}
