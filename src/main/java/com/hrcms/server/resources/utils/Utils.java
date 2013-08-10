package com.hrcms.server.resources.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class Utils {
    public static ByteArrayOutputStream read(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int size;
        while ((size = IOUtils.read(in, buf)) > 0) {
            baos.write(buf, 0, size);
        }
        return baos;
    }
}
