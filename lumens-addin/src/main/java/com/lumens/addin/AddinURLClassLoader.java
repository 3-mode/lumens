/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class AddinURLClassLoader extends URLClassLoader {

    public AddinURLClassLoader(URL[] url, ClassLoader parent) {
        super(url, parent);
    }

    public AddinURLClassLoader addJarLocationOrPathLoactionURL(URL url) {
        super.addURL(url);
        return this;
    }
}
