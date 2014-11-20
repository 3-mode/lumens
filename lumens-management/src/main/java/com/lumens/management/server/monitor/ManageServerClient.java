/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.management.server.monitor;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ManageServerClient {

    public static void main(String[] args) throws Exception {

        String JNI_Path = ClassLoader.getSystemResource(".").getPath() + "../../src/main/resources/sigar_jni";
        System.out.println(JNI_Path);
        JNI_Path = Paths.get(new File(JNI_Path).toURI()).normalize().toFile().getAbsolutePath();
        System.out.println(JNI_Path);

        System.setProperty("java.library.path", JNI_Path);
        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);
        fieldSysPath.set(null, null);

        Sigar s = new Sigar();
        CpuPerc cpu = s.getCpuPerc();
        CpuPerc[] cpus = s.getCpuPercList();
        while (true) {
            System.out.println(cpus.length);
            for (CpuPerc c : cpus) {
                System.out.println("=========================================>");
                System.out.println("Sys: " + CpuPerc.format(c.getSys()));
                System.out.println("User: " + CpuPerc.format(c.getUser()));
                System.out.println("Idle: " + CpuPerc.format(c.getIdle()));
                System.out.println("<=========================================");
            }
            Thread.sleep(1000);
        }

        /*
         OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
         System.out.println("Current OS Name: " + osMXBean.getName());
         System.out.println("Current OS Processors: " + osMXBean.getAvailableProcessors());
         int count = 0;
         while (count++ < 1000) {
         double v = osMXBean.getSystemLoadAverage();
         if (v > 0) {
         System.out.println("Current OS LoadAverage: " + v);
         break;
         }
         }
         MemoryMXBean memMXBean = ManagementFactory.getMemoryMXBean();
         System.out.println("Current OS HeapMemoryUsage: " + memMXBean.getHeapMemoryUsage());
         System.out.println("Current OS NonHeapMemoryUsage: " + memMXBean.getNonHeapMemoryUsage());//*/
    }
}
