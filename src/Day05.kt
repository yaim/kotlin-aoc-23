fun main() {
    data class Range(
        val destination: Long,
        val source: Long,
        val range: Long
    )

    data class RangeMap(
        private val ranges: List<Range>
    ) {
        val map: Map<LongRange, Range> = ranges.associateBy { it.source until it.source + it.range }

        fun resolve(seed: Long): Long {
            val range = map.entries.firstOrNull { seed in it.key }?.value

            return if (range == null) seed else {
                seed + range.destination - range.source
            }
        }
    }

    fun List<String>.toRangeMaps(): List<Range> {
        return map { line -> line.split(" ").map { it.toLong() } }.map { (d, s, r) -> Range(d, s, r) }
    }

    fun List<String>.parse(): Pair<List<Long>, List<RangeMap>> {
        val seeds = get(0).substringAfter(":").trim().split(" ").map { it.toLong() }
        val mapSize = count { it.contains("map") }
        val maps: ArrayList<RangeMap> = ArrayList(mapSize)

        parent@ for (i in 1 until size) {
            if (get(i).contains(":")) {
                for (j in i + 1 until size) {
                    if (j + 1 == size || get(j).isBlank()) {
                        maps.add(RangeMap(subList(i + 1, j).toRangeMaps()))
                        continue@parent
                    }
                }
            }
        }

        return seeds to maps
    }

    fun runThroughMaps(seed: Long, maps: List<RangeMap>): Long {
        if (maps.isEmpty()) return seed

        val resolved = maps.first().resolve(seed)

        return runThroughMaps(resolved, maps.subList(1, maps.size))
    }

    fun runThroughMaps(range: LongRange, maps: List<RangeMap>) {
        TODO()
    }

    fun part1(input: List<String>): Long {
        val (seeds, maps) = input.parse()

        return seeds.minOf { runThroughMaps(it, maps) }
    }

    fun part2(input: List<String>): Long {
        val (seedsRange, maps) = input.parse()

        var minLocation = Long.MAX_VALUE

        seedsRange.chunked(2).forEach { (start, range) ->
            for (seed in start until start + range) {
                val location = runThroughMaps(seed, maps)
                if (location < minLocation) minLocation = location
            }
        }

        return minLocation
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day05_test_1")) == 35L)
    check(part2(readInput("Day05_test_1")) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
