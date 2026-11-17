/*

MIT License

Copyright (c) 2023 Ilya Mikheev

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */

package io.github.mikheevshow.nmt.metrics

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit

class JvmNativeMemoryTrackingParserTest {
    private val parser = JvmNativeMemoryTrackingParser()

    @Test
    fun `parses realistic output with large values and CRLF line endings`() {
        val output = listOf(
            "Native Memory Tracking:",
            "Total: reserved=3221225472KB, committed=1536KB",
            "-                     Class (reserved=6568KB, committed=4140KB)",
            "                            (classes #665)",
            "                            (malloc=424KB, #1000) (peak=500KB #1200)",
            "                            (mmap: reserved=6144KB, committed=3716KB)",
            "-                    Thread (reserved=6868KB, committed=6868KB)",
            "                            (thread #15)",
            "                            (arena=61KB, #30)"
        ).joinToString("\r\n")

        val metrics = parser.parse(output)
        assertEquals(3221225472L, metrics["total.reserved.kb"])
        assertEquals(1.5, metrics["total.committed.mb"])
        assertEquals("665", metrics["class.classes.total"])
        assertEquals(424L, metrics["class.malloc.allocated.kb"])
        assertEquals("1000", metrics["class.malloc.allocations"])
        assertEquals(61L, metrics["thread.arena.allocated.kb"])
        assertEquals("15", metrics["thread.threads.total"])
    }
    @Test
    fun `parses a real HotSpot NMT summary`(@TempDir tempDir: Path) {
        val source = tempDir.resolve("NmtProbe.java")
        Files.writeString(source, """
            import java.lang.management.ManagementFactory;
            import javax.management.ObjectName;
            public class NmtProbe {
                public static void main(String[] args) throws Exception {
                    Object result = ManagementFactory.getPlatformMBeanServer().invoke(
                        new ObjectName("com.sun.management:type=DiagnosticCommand"), "vmNativeMemory",
                        new Object[]{new String[]{"summary"}}, new String[]{"[Ljava.lang.String;"});
                    System.out.print(result);
                }
            }
        """.trimIndent())
        val java = Path.of(System.getProperty("java.home"), "bin", "java").toString()
        val process = ProcessBuilder(java, "-XX:NativeMemoryTracking=summary", source.toString())
            .redirectErrorStream(true).start()
        if (!process.waitFor(30, TimeUnit.SECONDS)) {
            process.destroyForcibly()
            error("NMT probe timed out")
        }
        val output = process.inputStream.bufferedReader().readText()
        assertEquals(0, process.exitValue(), output)
        val metrics = parser.parse(output)
        check((metrics["total.reserved.kb"] as Long) > 0)
        check((metrics["total.committed.kb"] as Long) > 0)
        val collector = JvmNativeMemoryTrackingMetricsCollector(
            JvmNativeMemoryTrackingMetricsNameDescriptions(), parser,
            object : CommandLineExecutor {
                override fun execute(command: String, vararg args: String) = output
            }
        )
        assertEquals(metrics.size, collector.collect().size())
    }

}
