/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class Cpu {
    private final int sys;
    private final int user;
    private final int idle;
    private final int combined;

    public Cpu(double combined, double sys, double user, double idle) {
        this.combined = (int) Math.rint(100 * combined);
        this.sys = (int) Math.rint(100 * sys);
        this.user = (int) Math.rint(100 * user);
        this.idle = (int) Math.rint(100 * idle);
    }

    public int getSys() {
        return sys;
    }

    public int getUser() {
        return user;
    }

    public int getIdle() {
        return idle;
    }

    public int getCombined() {
        return combined;
    }
}
