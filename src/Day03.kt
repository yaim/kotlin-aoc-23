fun main() {
    data class Point(val x: Int, val y: Int)

    data class LineCoordinate(val start: Point, val end: Point) {
        val borderPoints by lazy {
            val x = start.x
            val y1 = start.y
            val y2 = end.y

            val point: MutableList<Point> = mutableListOf()

            for (y in y1 - 1..y2 + 1) {
                point.add(Point(x - 1, y))
                point.add(Point(x + 1, y))
            }

            point.add(Point(x, y1 - 1))
            point.add(Point(x, y2 + 1))

            point.toList()
        }

        fun isInTouchWith(point: Point) = point in borderPoints
    }

    fun extractNumbersCoordination(lines: List<String>): Map<LineCoordinate, Int> {
        val numbers: MutableMap<LineCoordinate, Int> = mutableMapOf()

        lines.mapIndexed { x, line ->
            var y = 0
            val iterator = line.iterator()

            while (iterator.hasNext()) {
                val char = iterator.next()
                if (char.isDigit()) {
                    val start = y
                    var end: Int? = null

                    while (iterator.hasNext()) {
                        val next = iterator.next()
                        if (next.isDigit()) {
                            y++
                            continue
                        } else {
                            end = y
                            y++
                            break
                        }
                    }

                    if (end == null) end = line.length - 1

                    numbers[LineCoordinate(Point(x, start), Point(x, end))] = line.substring(start, end + 1).toInt()
                }
                y++
            }
        }

        return numbers
    }

    fun toCharMap(lines: List<String>): Map<Point, Char> {
        return lines
            .mapIndexed { x, line -> line.mapIndexed { y, c -> Point(x, y) to c } }
            .flatten()
            .associate { it.first to it.second }
    }

    fun lineIsAdjacentToSymbol(charMap: Map<Point, Char>, coordinate: LineCoordinate) =
        charMap.filterKeys { it in coordinate.borderPoints }.any { it.value != '.' && !it.value.isDigit() }

    fun getRatioPairs(numbers: Map<LineCoordinate, Int>, gears: Set<Point>): List<Pair<Int, Int>> {
        return gears.map { gear ->
            gear to numbers.filterKeys { it.isInTouchWith(gear) }.values
        }.filter { it.second.size == 2 }.map { it.second.toIntArray()[0] to it.second.toIntArray()[1] }
    }

    fun part1(input: List<String>): Int {
        val charMap = toCharMap(input)
        val numbers = extractNumbersCoordination(input)

        return numbers.filterKeys { lineIsAdjacentToSymbol(charMap, it) }.map { it.value }.sum()
    }

    fun part2(input: List<String>): Int {
        val gears = toCharMap(input).filterValues { it == '*' }
        val numbers = extractNumbersCoordination(input)
        val ratioPairs: List<Pair<Int, Int>> = getRatioPairs(numbers, gears.keys)

        return ratioPairs.sumOf { it.first * it.second }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day03_test_1")) == 4361)
    check(part2(readInput("Day03_test_1")) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
