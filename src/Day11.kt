typealias Galaxy = Pair<Int, Int>

fun List<String>.galaxies() = flatMapIndexed { row: Int, line: String ->
    line.mapIndexedNotNull { col, ch ->
        if (ch == '#') {
            col to row
        } else null
    }
}
fun List<String>.linesWithOnlySpace() = mapIndexedNotNull { index, line ->
    if (line.all { it == '.' }) index else null
}
fun List<String>.columnsWithOnlySpace() = (0..<first().length).mapNotNull { col ->
    if (all { it[col] == '.' }) col else null
}

fun List<Galaxy>.product(): Set<Pair<Galaxy, Galaxy>> {
    val pairs = mutableSetOf<Pair<Galaxy, Galaxy>>()
    for (i in indices) {
        for (j in i + 1..<size) {
            pairs.add(get(i) to get(j))
        }
    }
    return pairs
}

fun Set<Pair<Galaxy, Galaxy>>.sumOfDistances(linesWithOnlySpace:List<Int>, columnsWithOnlySpace:List<Int>, factor:Long): Long {
    return sumOf { (galaxy, otherGalaxy) ->
        val x = listOf(galaxy.first, otherGalaxy.first).sorted().let { it[0]..it[1] }.toList()
        val y = listOf(galaxy.second, otherGalaxy.second).sorted().let { it[0]..it[1] }.toList()

        val additionalColumns = (factor - 1) * columnsWithOnlySpace.count {
            x.contains(it)
        }
        val additionalLines = (factor - 1) * linesWithOnlySpace.count {
            y.contains(it)
        }
        x.size - 1 + additionalLines + y.size - 1 + additionalColumns
    }
}
fun main() {

    fun part1(input: List<String>): Long {
        val galaxies = input.galaxies()
        val linesWithOnlySpace = input.linesWithOnlySpace()
        val columnsWithOnlySpace = input.columnsWithOnlySpace()
        return galaxies.product().sumOfDistances(linesWithOnlySpace, columnsWithOnlySpace, 2L)
    }

    fun part2(input: List<String>): Long {
        val galaxies = input.galaxies()
        val linesWithOnlySpace = input.linesWithOnlySpace()
        val columnsWithOnlySpace = input.columnsWithOnlySpace()
        return galaxies.product().sumOfDistances(linesWithOnlySpace, columnsWithOnlySpace, 1000000L)
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day11_test")
    part1(testInput1).println()
    check(part1(testInput1) == 374L)

    val input = readInput("Day11")
    part1(input).println()
    check(part1(input) == 9686930L)

    part2(input).println()
    check(part2(input) == 630728425490L)
}
