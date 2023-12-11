fun main() {
    data class Galaxy(val x: Int, val y: Int)

    data class CharMatrix(val lines: List<List<Char>>) {
        val transpose by lazy { CharMatrix((lines[0].indices).map { i -> (lines.indices).map { j -> lines[j][i] } }) }
    }

    fun List<String>.toMatrix() = CharMatrix(map { it.toList() })

    fun List<Char>.filterGalaxies(x: Int) =
        mapIndexed { y, char -> if (char == '#') return@mapIndexed Galaxy(x, y) else null }.filterNotNull()

    fun CharMatrix.filterGalaxies() = lines.mapIndexed { x, row -> row.filterGalaxies(x) }.flatten()

    fun CharMatrix.emptySpaceBetween(x1: Int, x2: Int) =
        lines.drop(x1).dropLast(lines.size - x2 - 1).filter { line -> line.none { char -> char == '#' } }.size

    fun CharMatrix.distanceTo(a: Galaxy, b: Galaxy, expansionRate: Long = 1): Long {
        val (minX, maxX) = listOf(a.x, b.x).run { sorted() }
        val (minY, maxY) = listOf(a.y, b.y).run { sorted() }

        val emptyX = emptySpaceBetween(minX, maxX)
        val emptyY = transpose.emptySpaceBetween(minY, maxY)

        val xDistance = (expansionRate - 1) * emptyX + maxX - minX
        val yDistance = (expansionRate - 1) * emptyY + maxY - minY

        return xDistance + yDistance
    }

    fun List<Galaxy>.pairs(): List<Pair<Galaxy, Galaxy>> = buildList {
        this@pairs.forEachIndexed { i, galaxy ->
            addAll(this@pairs.takeLast(this@pairs.size - i - 1).map { other -> galaxy to other })
        }
    }

    fun part1(input: List<String>) = input.toMatrix().let { matrix ->
        matrix.filterGalaxies().pairs().sumOf { (a, b) -> matrix.distanceTo(a, b, 2) }
    }

    fun part2(input: List<String>, expansionRate: Long = 1000000) = input.toMatrix().let { matrix ->
        matrix.filterGalaxies().pairs().sumOf { (a, b) -> matrix.distanceTo(a, b, expansionRate) }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day11_test_1")) == 374L)
    check(part2(readInput("Day11_test_1"), 10) == 1030L)
    check(part2(readInput("Day11_test_1"), 100) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
