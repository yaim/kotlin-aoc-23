fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { "${it.firstDigitChar()}${it.reversed().firstDigitChar()}".toInt() }
    }

    fun part2(input: List<String>): Int {
        return input.map(String::filterDigits)
            .sumOf { "${it.firstDigitChar()}${it.reversed().firstDigitChar()}".toInt() }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day01_test_1")) == 142)
    check(part2(readInput("Day01_test_2")) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
