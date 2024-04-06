data class Cube(val col: Int, val row: Int, var roundRocks: Int = 0)
fun main() {


    class Platform(val columns: Int, val rows: Int, cubeShapedRocks: List<Cube>, val roundRocks: List<Pair<Int, Int>>) {
        val cubeShapedRocks = cubeShapedRocks.union((0..<columns).map {
            Cube(it, -1)
        })
        fun tiltNorth(): Platform {
            (0..<columns).map { column ->
                roundRocks.filter { it.first == column }.mapNotNull { (_, row) ->
                    cubeShapedRocks.union(listOf(Cube(column, -1))).filter {
                        it.col == column && it.row < row
                    }.maxByOrNull { it.row }?.let { cube ->
                        cube.roundRocks++
                    }
                }
            }
            return this
        }

        fun load(cube: Cube): Int {
            return ((rows-cube.row-cube.roundRocks)..(rows-cube.row-1)).toList().sum()
        }
        fun load(): Int {
            return cubeShapedRocks.filter { it.roundRocks > 0 }.sumOf { cube ->
                load(cube)
            }
        }
    }
    fun part1(input: List<String>): Int {
        val cubeShapedRocks = input.flatMapIndexed { row: Int, line: String ->
            line.mapIndexedNotNull { col, c ->
                if(c == '#') {
                    Cube(col, row, 0)
                } else null
            }
        }

        val roundRocks = input.flatMapIndexed { row: Int, line: String ->
            line.mapIndexedNotNull { col, c ->
                if(c == 'O') {
                    col to row
                } else null
            }
        }

        val platform = Platform(input.first().length, input.size, cubeShapedRocks, roundRocks)
        return platform.tiltNorth().load()
    }


    fun part2(input: List<String>): Int = TODO()

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day14_test")
    part1(testInput1).println()
    check(part1(testInput1) == 136)

    val input = readInput("Day14")
    part1(input).println()
    check(part1(input) == 113525)

    part2(testInput1).println()
    check(part2(testInput1) == TODO())

    part2(input).println()
    check(part2(input) == TODO())
}
