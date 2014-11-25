/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class Memory {
    private final int usedMem;
    private final int freeMem;
    private final long ram;

    public Memory(double usedMem, double freeMem, long ram) {
        this.usedMem = (int) Math.rint(usedMem * 100);
        this.freeMem = (int) Math.rint(freeMem * 100);
        this.ram = ram/1024/1024; // M
    }

    /**
     * @return the usedMem
     */
    public int getUsedMem() {
        return usedMem;
    }

    /**
     * @return the freeMem
     */
    public int getFreeMem() {
        return freeMem;
    }

    public long getRAM() {
        return ram;
    }
}
