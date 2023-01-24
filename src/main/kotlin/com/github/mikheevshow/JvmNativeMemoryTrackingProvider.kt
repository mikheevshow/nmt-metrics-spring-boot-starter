package com.github.mikheevshow

import java.lang.management.ManagementFactory
import javax.management.ObjectName

class JvmNativeMemoryTrackingProvider {

    private val mBeanServer = ManagementFactory.getPlatformMBeanServer()

    fun getNmtDescription(): String {
        return mBeanServer.invoke(
            ObjectName("com.sun.management:type=DiagnosticCommand"),
            "vmNativeMemory",
            arrayOf("summary"),
            arrayOf("[Ljava.lang.String;")
        ) as String
    }
}