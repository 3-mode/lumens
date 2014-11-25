/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.test;

import com.lumens.management.server.monitor.Cpu;
import com.lumens.management.server.monitor.Memory;
import com.lumens.management.server.monitor.OSResourcesMonitor;
import com.lumens.management.server.monitor.ServerManagementFactory;
import java.io.File;
import java.nio.file.Paths;
import org.junit.Test;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class MonitorJUnitTest {

    public MonitorJUnitTest() {
    }

    @Test
    public void testSigarFeatures() throws Exception {
        String JNI_Path = ClassLoader.getSystemResource(".").getPath() + "../../src/main/resources/sigar_jni";
        JNI_Path = Paths.get(new File(JNI_Path).toURI()).normalize().toFile().getAbsolutePath();
        System.setProperty("java.library.path", JNI_Path);
        OSResourcesMonitor os = ServerManagementFactory.get().createOSResourcesMonitor();
        System.out.println(os.getCpuUsage());
        int cpuCount = os.getCpuCount();
        Cpu[] cpus = os.gatherCpuPerc();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cpuCount; ++i) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(String.format("{ \"combined\"  : %d,", cpus[i].getCombined()));
            sb.append(String.format("  \"sys\" : %d,", cpus[i].getSys()));
            sb.append(String.format("  \"user\" : %d,", cpus[i].getUser()));
            sb.append(String.format("  \"idle\" : %d  }", cpus[i].getIdle()));
        }
        System.out.println(sb.toString());

        Memory mem = os.getMemPerc();
        sb = new StringBuilder();
        sb.append(String.format("{ \"used\"  : %d,", mem.getUsedMem()));
        sb.append(String.format("  \"free\" : %d,", mem.getFreeMem()));
        sb.append(String.format("  \"ram\" : %f  }", mem.getRAM()/1024.0));
        System.out.println(String.format("{ "
                                         + "\"memory\" : %s "
                                         + "}", sb.toString()));
    }
}
