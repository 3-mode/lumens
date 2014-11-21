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

    public int getSys() {
        return sys;
    }

    public int getUser() {
        return user;
    }

    public int getIdle() {
        return idle;
    }

    public Cpu(double sys, double user, double idle) {
        this.sys = (int) Math.rint(100 * sys);
        this.user = (int) Math.rint(100 * user);
        this.idle = (int) Math.rint(100 * idle);
    }
}
