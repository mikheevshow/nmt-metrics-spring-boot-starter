package com.github.mikheevshow

class JvmNativeMemoryTrackingParser {

    private val NMT_START_RE = "^[0-9]+\\:".toRegex()
    private val NMT_TOTAL_RE = "^Total: reserved=(.*), committed=(.*)".toRegex()
    private val NMT_SECTION_RE = "^-\\s+([A-Za-z0-9 -]+) \\(reserved=(.*), committed=(.*)\\)".toRegex()
    private val NMT_SECTION_MMAP_RE = "\\(mmap: reserved=(.*), committed=(.*)\\)".toRegex()
    private val NMT_CLASSES_RE = "(classes #([0-9]+))".toRegex()
    private val NMT_CLASS_INSTANCES_RE = "\\( *instance classes \\#([0-9]+), array classes \\#([0-9]+)\\)".toRegex()
    private val NMT_MALLOC_RE = "\\(malloc=(.*) #([0-9]+)\\)".toRegex()
    private val NMT_THREADS_RE = "\\(thread #([0-9]+)\\)".toRegex()
    private val NMT_STATS_SECTION_START_RE = "\\(  ([a-zA-Z ]+)\\: *\\)".toRegex()
    private val NMT_STATS_SECTION_MEMORY_RE = "\\(    reserved=(.*), committed=(.*)\\)".toRegex()
    private val NMT_STATS_SECTION_ATTR_RE = "\\(    ([a-zA-Z ]+)=([0-9]+[A-Z]+)\\)".toRegex()
    private val NMT_STATS_SECTION_ATTR_PERCENTAGE_RE = "\\(    ([a-zA-Z ]+)=([0-9]+[A-Z]+) .([0-9\\.\\%]+)\\)".toRegex()
    private val NMT_ARENA_RE = "\\(arena=(.*) \\#([0-9]+)\\)".toRegex()
    private val NMT_STACK_RE = "\\(stack: reserved=(.*), committed=(.*)\\)".toRegex()

    fun parse(s: String): Map<String, Any> {
        val lines = s.split(System.getProperty("line.separator"))

        val map = HashMap<String, Any>()

        var section = ""
        var innerSection = ""
        var attr = ""
        var groups: MatchGroupCollection?
        for (line in lines) {

            if (line.matches(NMT_START_RE) || line.contains("Native Memory Tracking:") || line.isEmpty()) {
                section = ""
                continue
            }

            groups = NMT_TOTAL_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                memory(map, "total.reserved", groups[1]!!.value)
                memory(map, "total.committed", groups[2]!!.value)
                continue
            }

            groups = NMT_SECTION_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                section = groups[1]!!.value.slugify()
                memory(map, "$section.reserved", groups[2]!!.value)
                memory(map, "$section.committed", groups[3]!!.value)
                continue
            }

            groups = NMT_SECTION_MMAP_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                memory(map, "$section.mmap.reserved", groups[1]!!.value)
                memory(map, "$section.mmap.committed", groups[2]!!.value)
                continue
            }

            groups = NMT_CLASSES_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                map["$section.classes.total"] = groups[1]!!.value
                continue
            }

            groups = NMT_CLASS_INSTANCES_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                map["$section.classes.instances"] = groups[1]!!.value
                map["$section.classes.arrays"] = groups[2]!!.value
                continue
            }

            groups = NMT_MALLOC_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                memory(map, "$section.malloc.allocated", groups[1]!!.value)
                map["$section.malloc.allocations"] = groups[2]!!.value
                continue
            }



            groups = NMT_STATS_SECTION_START_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                innerSection = groups[1]!!.value.slugify()
                continue
            }

            groups = NMT_STATS_SECTION_MEMORY_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                memory(map, "$section.$innerSection.reserved", groups[1]!!.value)
                memory(map, "$section.$innerSection.committed", groups[2]!!.value)
                continue
            }

            groups = NMT_STATS_SECTION_ATTR_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                attr = groups[1]!!.value.slugify()
                memory(map, "$section.$innerSection.$attr", groups[2]!!.value)
                continue
            }

            groups = NMT_STATS_SECTION_ATTR_PERCENTAGE_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                attr = groups[1]!!.value.slugify()
                memory(map, "$section.$innerSection.$attr", groups[2]!!.value)
                map["$section.$innerSection.$attr.percentage"] = groups[3]!!.value
                continue
            }

            groups = NMT_ARENA_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                memory(map, "$section.arena.allocated", groups[1]!!.value)
                map["$section.arena.allocations"] = groups[2]!!.value
                continue
            }

            groups = NMT_THREADS_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                map["$section.threads.total"] = groups[1]!!.value
                continue
            }

            groups = NMT_STACK_RE.find(line)?.groups
            if (groups?.isNotEmpty() == true) {
                memory(map, "$section.stack.reserved", groups[1]!!.value)
                memory(map, "$section.stack.committed", groups[2]!!.value)
                continue
            }
        }

        return map
    }

    private fun memory(map: HashMap<String, Any>, key: String, value: String) {
        val valueAsInt = value.substringBeforeLast("KB").toInt()
        map[key] = valueAsInt
        map["$key.kb"] = valueAsInt
        map["$key.mb"] = valueAsInt / 1024
        map["$key.gb"] = valueAsInt / 1024 / 1024
    }
}