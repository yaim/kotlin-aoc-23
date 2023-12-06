fun main() {
    data class Record(val time: Long, val distance: Long) {
        fun winningWays(): Long {
            var ways = if (time % 2L == 0L && time * time / 4 > distance) -1L else 0L

            for (i in time / 2 downTo 1) {
                if (i * (time - i) > distance) ways += 2
                else break
            }

            return ways
        }
    }

    fun List<String>.parse(): List<Record> {
        val times = get(0).substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        val distances = get(1).substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toLong() }

        return times.mapIndexed { index, time -> Record(time, distances[index]) }
    }

    fun List<Long>.product(): Long = reduce { acc, i -> acc * i }

    fun part1(input: List<String>): Long {
        return input.parse().map { it.winningWays() }.product()
    }

    fun part2(input: List<String>): Long {
        val time = input[0].replace(" ", "").substringAfter(":").toLong()
        val distance = input[1].replace(" ", "").substringAfter(":").toLong()

        return Record(time, distance).winningWays()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day06_test_1")) == 288L)
    check(part2(readInput("Day06_test_1")) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
