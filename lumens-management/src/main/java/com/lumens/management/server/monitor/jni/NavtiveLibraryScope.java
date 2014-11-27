/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor.jni;

import java.util.Vector;

public class NavtiveLibraryScope {

    private static java.lang.reflect.Field LIBRARIES;

    static {
        try {
            LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            LIBRARIES.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean libraryContains(final ClassLoader loader, String libraryName) {
        try {
            Vector<String> libraries = (Vector<String>) LIBRARIES.get(loader);
            return libraries.contains(libraryName);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeAll(ClassLoader loader) {
        try {
            Vector<String> libraries = (Vector<String>) LIBRARIES.get(loader);
            libraries.clear();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeLibraryName(ClassLoader loader, String libraryName) {
        try {
            if (libraryContains(loader, libraryName)) {
                Vector<String> libraries = (Vector<String>) LIBRARIES.get(loader);
                libraries.remove(libraryName);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
