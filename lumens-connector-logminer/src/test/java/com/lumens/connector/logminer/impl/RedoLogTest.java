/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.logminer.impl;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RedoLogTest extends TestBase {

    @Test
    public void testGetOnlineFileList() throws Exception {
        assertTrue(sourceDatabase != null);
        RedoLog redoLog = new RedoLog(sourceDatabase);
        List<String> onlineList = redoLog.getOnlineFileList();
        assertTrue("online redo log should not be empty", onlineList.size() > 0);
        System.out.println("Online redo log list:");
        for (String item : onlineList) {
            System.out.println(item);
        }
        System.out.println("Offline redo log list:");
        for (String item : redoLog.getOfflineFileList()) {
            System.out.println(item);
        }
    }
}
