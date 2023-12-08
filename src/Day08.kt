fun main() {
    fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)

    fun lcm(x: Long, y: Long) = x * y / gcd(x, y)

    fun countSteps(
        map: Map<String, Pair<String, String>>,
        path: String,
        start: String,
        endings: List<String>
    ): Long {
        var current = start
        var counter = 0L

        while (current !in endings) {
            val index = (counter % path.length).toInt()

            current = when (path[index]) {
                'L' -> map[current]!!.first
                'R' -> map[current]!!.second
                else -> error("Invalid Char")
            }

            counter++
        }

        return counter
    }

    fun countSteps(
        map: Map<String, Pair<String, String>>,
        path: String,
        startings: List<String>,
        endings: List<String>
    ): Long = startings.map { countSteps(map, path, it, endings) }.reduce { a, b -> lcm(a, b) }

    fun List<String>.parse(): Map<String, Pair<String, String>> = associate { line ->
        val (key, sides) = line.split(" = ")
        val (left, right) = sides.removeSurrounding("(", ")").split(", ")
        Pair(key, Pair(left, right))
    }

    fun part1(input: List<String>): Long {
        val path = input[0]
        val map = input.drop(2).parse()

        val start = map.keys.first { it == "AAA" }
        val end = map.keys.first { it == "ZZZ" }

        return countSteps(map, path, start, listOf(end))
    }

    fun part2(input: List<String>): Long {
        val path = input[0]
        val map = input.drop(2).parse()

        val startingNodes = map.keys.filter { it.endsWith('A') }
        val endingNodes = map.keys.filter { it.endsWith('Z') }

        return countSteps(map, path, startingNodes, endingNodes)
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day08_test_1")) == 6L)
    check(part2(readInput("Day08_test_2")) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
