import java.util.*

enum class CubeColor {
    BLUE,
    GREEN,
    RED
}

fun RandomCubes.power() : Int = (get(CubeColor.RED) ?: 0) * (get(CubeColor.BLUE) ?: 0) * (get(CubeColor.GREEN) ?: 0)

typealias Game = Pair<Int, List<RandomCubes>>
typealias RandomCubes = Map<CubeColor, Int>
fun main() {

    fun String.parse(): Game = split(": ").let { (f, l) ->
        f.substring(5).toInt() to l.split("; ").map {
            it.split(", ").map {
                it.split(" ").let {(num, col) ->
                    CubeColor.valueOf(col.uppercase(Locale.getDefault())) to num.toInt()
                }
            }.toMap()
        }
    }
    fun part1(input: List<String>): Int {
        return input.map {
            line -> line.parse()
        }.filter { game: Game ->
            game.second.all {
                it[CubeColor.RED]?.let { it <= 12 } ?: true &&
                it[CubeColor.BLUE]?.let { it <= 14 } ?: true &&
                it[CubeColor.GREEN]?.let { it <= 13 } ?: true
            }
        }.sumOf {
            it.first
        }
    }


    fun part2(input: List<String>): Int {

        return input.map {
                line -> line.parse()
        }.map { game: Game ->
            mapOf(
                CubeColor.RED to game.second.maxOf { it[CubeColor.RED] ?: 0 },
                CubeColor.BLUE to game.second.maxOf { it[CubeColor.BLUE] ?: 0 },
                CubeColor.GREEN to game.second.maxOf { it[CubeColor.GREEN] ?: 0 }
            )
        }.sumOf {
            it.power()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day02_test")
    part1(testInput1).println()
    check(part1(testInput1) == 8)

    val input = readInput("Day02")
    part1(input).println()
    check(part1(input) == 2406)

    part2(testInput1).println()
    check(part2(testInput1) == 2286)

    part2(input).println()
    check(part2(input) == 78375)
}
