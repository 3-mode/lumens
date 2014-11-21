/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.test;

import com.lumens.management.server.monitor.Cpu;
import com.lumens.management.server.monitor.OSResourcesMonitor;
import com.lumens.management.server.monitor.ServerManagementFactory;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Arrays;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
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
        System.out.println(JNI_Path);
        JNI_Path = Paths.get(new File(JNI_Path).toURI()).normalize().toFile().getAbsolutePath();
        System.out.println(JNI_Path);

        OSResourcesMonitor os = ServerManagementFactory.createOSResourcesMonitor(JNI_Path);
        os.getCpuCount();
        int cpuCount = os.getCpuCount();
        Cpu[] cpus = os.gatherCpuPerc();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cpuCount; ++i) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(String.format("{ 'sys'  : %d,", cpus[i].getSys()));
            sb.append(String.format("  'user' : %d,", cpus[i].getUser()));
            sb.append(String.format("  'idle' : %d  }", cpus[i].getIdle()));
        }
        System.out.println(sb.toString());
    }
}
