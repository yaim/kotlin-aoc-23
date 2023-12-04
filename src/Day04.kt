fun main() {
    fun String.matchCount(): Int {
        val (winning, match) = substringAfter(":").trim().split("|").map { sub ->
            sub.trim().replace("\\s+".toRegex(), " ").split(" ").map { it.toInt() }
        }

        return match.count { winning.contains(it) }
    }

    fun part1(input: List<String>): Int {
        val points = input.map(String::matchCount).filter { it >= 1 }.map { 2.power(it - 1).toInt() }

        return points.sum()
    }

    fun part2(input: List<String>): Int {
        val cards = IntArray(input.size).apply { fill(1) }

        input.map(String::matchCount).forEachIndexed { i, count ->
            for (index in i + 1 .. i + count) cards[index] += cards[i]
        }

        return cards.sum()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day04_test_1")) == 13)
    check(part2(readInput("Day04_test_1")) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
