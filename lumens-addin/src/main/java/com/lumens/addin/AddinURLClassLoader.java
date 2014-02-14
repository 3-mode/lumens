/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import com.lumens.LumensException;
import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class AddinURLClassLoader extends URLClassLoader {

    public static FilenameFilter jarFliter = new JarFilenameFilter();
    public static FilenameFilter lumensJarFilter = new LumensJarFilenameFilter();

    protected static class JarFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith("jar");
        }
    }

    protected static class LumensJarFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().startsWith("lumens-");
        }
    }

    public AddinURLClassLoader(URL[] url, ClassLoader parent) {
        super(url, parent);
    }

    public AddinURLClassLoader addJarLocationOrPathLoactionURL(URL url) {
        try {
            File urlFile = new File(url.toURI());
            if (urlFile.isFile())
                super.addURL(url);
            else if (urlFile.isDirectory()) {
                for (File jarFile : urlFile.listFiles(jarFliter))
                    super.addURL(jarFile.toURI().toURL());
            }
            return this;
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new LumensException(ex);
        }
    }
}
