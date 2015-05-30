/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.fastsync.test;

import com.lumens.connector.fastsync.impl.Dictionary;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DictionaryTest extends FastSyncTestBase {

    @Test
    public void testDictionary() throws Exception {
        assertTrue(sourceDatabase != null);
        Dictionary dict = new Dictionary(sourceDatabase);
        assertTrue("dictionary should not be empty", !dict.getDictionaryPath().isEmpty());
        try {
            dict.build();
        } catch (Exception ex) {
            System.out.println("Fail to create dictionary file. Error message:" );
            System.out.println(ex.getMessage());
        }
    }
}
