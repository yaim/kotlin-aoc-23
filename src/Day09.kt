fun main() {
    fun List<String>.parse() = map { line -> line.split(" ").map { it.toLong() } }

    fun List<Long>.listDiffs() = foldIndexed(mutableListOf<Long>()) { index, list, current ->
        val next = getOrNull(index + 1)
        if (next != null) list.add(next - current)
        list
    }.toList()

    fun List<Long>.process(last: Boolean): Long {
        if (all { it == 0L }) return 0

        return if (last) last() + listDiffs().process(true)
        else first() - listDiffs().process(false)
    }

    fun part1(input: List<String>): Long {
        return input.parse().sumOf { it.process(true) }
    }

    fun part2(input: List<String>): Long {
        return input.parse().sumOf { it.process(false) }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day09_test_1")) == 114L)
    check(part2(readInput("Day09_test_1")) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
