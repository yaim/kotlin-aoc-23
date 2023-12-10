import PipeDirection.Companion.eastConnections
import PipeDirection.Companion.northConnections
import PipeDirection.Companion.southConnections
import PipeDirection.Companion.westConnections

enum class PipeDirection(val c: Char) {
    VERTICAL('|'),
    HORIZONTAL('-'),
    NE_BEND('L'),
    NW_BEND('J'),
    SW_BEND('7'),
    SE_BEND('F'),

    //    GROUND('.'),
    STARTING_POINT('S');

    companion object {
        private val map = entries.associateBy { it.c }

        val northConnections = listOf(STARTING_POINT, VERTICAL, SW_BEND, SE_BEND)
        val southConnections = listOf(STARTING_POINT, VERTICAL, NE_BEND, NW_BEND)
        val eastConnections = listOf(STARTING_POINT, HORIZONTAL, SW_BEND, NW_BEND)
        val westConnections = listOf(STARTING_POINT, HORIZONTAL, SE_BEND, NE_BEND)

        fun fromChar(c: Char) = map[c]
    }
}

fun main() {
    fun Char.toDirection() = PipeDirection.fromChar(this)

    fun Char.toAscii() = when {
        this == '|' -> '│'
        this == '-' -> '─'
        this == 'L' -> '╰' // '└'
        this == 'J' -> '╯' // '┘'
        this == '7' -> '╮' // '┐'
        this == 'F' -> '╭' // '┌'
        else -> this
    }

    data class Point(val x: Int, val y: Int) {
        fun surroundings(): List<Point> = listOf(copy(x = x - 1), copy(y = y + 1), copy(x = x + 1), copy(y = y - 1))
    }

    fun List<String>.getDirection(x: Int, y: Int) = getOrNull(x)?.getOrNull(y)?.toDirection()

    fun List<String>.getDirection(point: Point) = getDirection(point.x, point.y)

    fun List<String>.startPoint() = indexOfFirst { it.contains('S') }.let { Point(it, get(it).indexOf('S')) }

    fun List<String>.getNextPoints(current: Point, prev: Point): List<Point> = buildList {
        val (n, e, s, w) = current.surroundings()

        getDirection(current).let { direction ->
            if (direction in southConnections && getDirection(n) in northConnections) add(n)
            if (direction in westConnections && getDirection(e) in eastConnections) add(e)
            if (direction in eastConnections && getDirection(w) in westConnections) add(w)
            if (direction in northConnections && getDirection(s) in southConnections) add(s)
        }

        remove(prev)
    }

    tailrec fun List<String>.traverse(path: List<Point>): List<Point> {
        val next = getNextPoints(path.last(), path.takeLast(2).first()).first()

        if (next == startPoint()) return path

        return traverse(path + next)
    }

    fun List<String>.getPath() = traverse(listOf(startPoint()))

    fun List<String>.transpose() =
        (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] }.joinToString("") }

    fun List<String>.prettify() = mapIndexed { x, line ->
        val path = getPath()
        line.mapIndexed { y, char -> if (Point(x, y) in path) char.toAscii() else '.' }.joinToString("")
    }

    fun List<String>.expand() = map { line ->
        val connector = line.map { char -> if (char in listOf('│', '╮', '╭', 'S')) '│' else ' ' }.joinToString("")
        listOf(line, connector)
    }.flatten().transpose().let { transpose ->
        transpose.mapIndexed { i, line ->
            val nextLine = transpose.getOrNull(i + 1) ?: " ".repeat(line.length)
            val connector = line.mapIndexed { j, char ->
                if (char in listOf('─', '╰', '╭') || (char == 'S' && nextLine[j] in listOf('╮', '─', '╯'))) '─'
                else ' '
            }.joinToString("")
            listOf(line, connector)
        }
    }.flatten().transpose()

    fun List<String>.draw() = forEach { line -> println(line) }

    fun List<String>.drawExpanded() = prettify().expand().draw()

    fun List<String>.searchForDots(start: Point): Int {
        val traversedChars = mutableListOf<Char>()
        val visited = Array(size) { BooleanArray(this[0].length) }

        fun isValid(x: Int, y: Int) = this[x][y] in listOf(' ', '.') && !visited[x][y]

        fun dfs(x: Int, y: Int) {
            if (!isValid(x, y)) {
                return
            }

            visited[x][y] = true
            traversedChars.add(this[x][y])

            val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            for ((dx, dy) in directions) {
                val newX = x + dx
                val newY = y + dy
                if (isValid(newX, newY)) {
                    dfs(newX, newY)
                }
            }
        }

        dfs(start.x, start.y)

        return traversedChars.filter { it == '.' }.size
    }

    fun part1(input: List<String>) = input.getPath().size / 2

    fun part2(input: List<String>, traverseStart: (Point) -> Point) =
        input.prettify().expand().let { it.searchForDots(traverseStart(it.startPoint())) }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day10_test_1")) == 8)
    check(part2(readInput("Day10_test_2")) { p -> p.copy(x = p.x - 1) } == 8)
    check(part2(readInput("Day10_test_3")) { p -> p.copy(x = p.x + 1, y = p.y - 1) } == 10)

    val input = readInput("Day10").also { it.drawExpanded() }
    part1(input).println()
    part2(input) { p -> p.copy(y = p.y - 1) }.println()
}
