fun main() {
    fun part1(input: List<String>): Int {
        return input.map { parseLine(it) }.filter { inBoundary(it) }.sumOf { it.first }
    }

    fun part2(input: List<String>): Int {
        return input.map { parseLine(it) }.map { toPower(it) }.sumOf { it }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day02_test_1")) == 8)
    check(part2(readInput("Day02_test_1")) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

fun inBoundary(pair: Pair<Int, Colors>): Boolean = pair.second.run { return r <= 12 && b <= 14 && g <= 13 }

fun toPower(pair: Pair<Int, Colors>): Int = pair.second.run { return r * b * g }

fun parseLine(line: String): Pair<Int, Colors> {
    val colors = Colors()
    val l = line.lowercase()
    val splits = l.split(":")
    val id = splits[0].replaceFirst("game", "").trim().toInt()

    splits[1].trim().replace(";", ",").split(",").forEach { comb ->
        val c = comb.trim().split(" ")
        val num = c[0]
        val color = c[1]

        when (color) {
            "red" -> if(num.toInt() > colors.r) colors.r = num.toInt()
            "blue" -> if(num.toInt() > colors.b) colors.b = num.toInt()
            "green" -> if(num.toInt() > colors.g) colors.g = num.toInt()
        }
    }

    return id to colors
}

data class Colors(var r: Int = 0, var b: Int = 0, var g: Int = 0)