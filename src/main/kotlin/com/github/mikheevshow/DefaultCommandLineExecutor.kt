package com.github.mikheevshow

import java.lang.management.ManagementFactory
import javax.management.ObjectName

interface CommandLineExecutor {
    fun execute(command: String, vararg args: String): String
}

class DefaultCommandLineExecutor: CommandLineExecutor {

    private val mBeanServer = ManagementFactory.getPlatformMBeanServer()

    override fun execute(command: String, vararg args: String): String {
        return mBeanServer.invoke(
            ObjectName("com.sun.management:type=DiagnosticCommand"),
            command,
            arrayOf(args),
            arrayOf("[Ljava.lang.String;")
        ) as String
    }
}

fun CommandLineExecutor.getNmtSummary(): String = execute("vmNativeMemory", "summary")