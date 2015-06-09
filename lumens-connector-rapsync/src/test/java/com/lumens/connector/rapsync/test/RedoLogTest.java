/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync.test;

import com.lumens.connector.rapsync.impl.RedoLog;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RedoLogTest extends RapSyncTestBase {

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

    @Test
    public void testScn() {
        assertTrue(sourceDatabase != null);
        RedoLog redoLog = new RedoLog(sourceDatabase);

        // test get mnin scn
        String minScn = redoLog.getMinSCN();
        assertTrue("Fail to get min SCN. ", minScn != null);
        if (minScn != null) {
            System.out.println("Min scn is " + minScn);
        }

        // Test valid scn
        boolean valid = redoLog.isValidSCN(minScn);
        if (valid) {
            System.out.println(String.format("%s is not a valid scn", minScn));
        }
        assertTrue("1664831 Not a valid SCN", valid);

        // test get current scn
        String curScn = redoLog.getCurrentSCN();
        assertTrue("Fail to get current SCN. ", curScn != null);
        if (curScn != null) {
            System.out.println("current scn is " + curScn);
        }
    }
}
