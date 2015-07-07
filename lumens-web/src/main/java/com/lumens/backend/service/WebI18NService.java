/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.backend.service;

import com.lumens.backend.ApplicationContext;
import static com.lumens.backend.ServiceConstants.UTF_8;
import com.lumens.logsys.SysLogFactory;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
@Path("/i18n")
public class WebI18NService {

    private static final Logger log = SysLogFactory.getLogger(WebI18NService.class);

    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response i18nJSon() {
        try (InputStream in = WebI18NService.class.getResourceAsStream("i18n/" + ApplicationContext.get().getLang() + ".json")) {
            String i18n = IOUtils.toString(in, UTF_8);
            return Response.ok(i18n).build();
        } catch (IOException e) {
            log.error(e);
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
