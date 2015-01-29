/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.io;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class Utils {
    public static long generateID() {
        long ID1 = System.currentTimeMillis(), ID2;
        do {
            ID2 = System.currentTimeMillis();
        } while (ID1 == ID2);
        return ID2;
    }
}
