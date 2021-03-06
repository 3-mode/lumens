/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.test;

import com.lumens.connector.rapsync.impl.Dictionary;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class DictionaryTest extends RapSyncTestBase {

    @Test
    public void testDictionary() throws Exception {
        assertTrue(sourceDatabase != null);
        Dictionary dict = new Dictionary(sourceDatabase);        
        try {
            if(!dict.getDictionaryPath().isEmpty()){
                dict.build();
            }
        } catch (Exception ex) {
            System.out.println("Fail to create dictionary file. Error message:" );
            System.out.println(ex.getMessage());
        }
    }
}
