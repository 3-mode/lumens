/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DictionaryTest extends TestBase {

    @Test
    public void testDictionary() throws Exception {
        assertTrue(dbClient != null);
        Dictionary dict = new Dictionary(dbClient);
        assertTrue("dictionary should not be empty", !dict.getDictionaryPath().isEmpty());
        try {
            dict.createDictionary();
        } catch (Exception ex) {
            System.out.println("Fail to create dictionary file. Error message:" );
            System.out.println(ex.getMessage());
        }
    }
}
