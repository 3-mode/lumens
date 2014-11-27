/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class Disk {
    private final String devName;
    private final long total;
    private final int usePercent;

    public Disk(String devName, long total, double usePercent) {
        this.devName = devName.replace("\\", "\\\\"); // If there is \ then replace it as \\
        this.total = total / 1024; // M
        this.usePercent = (int) Math.rint(100 * usePercent);
    }

    public String getDevName() {
        return devName;
    }

    public long getTotal() {
        return total;
    }

    public int getUsePercent() {
        return usePercent;
    }

}
