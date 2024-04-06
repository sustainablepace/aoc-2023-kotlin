import kotlin.math.min

fun main() {

    fun part1(input: List<String>): Int {
        val matrices = input.joinToString("\n")
            .replace(".", "0")
            .replace("#", "1")
            .split("\n\n")
            .map { it.split("\n") }.map {
                it to it.first().mapIndexed { index, c ->
                    it.map { it[index] }.joinToString("")
                }.reversed()
            }

        fun List<String>.calc() = (0..<first().length).zipWithNext().filter { (row, nextRow) ->
            map { line ->
                val len = min(row + 1, line.length - nextRow)
                val left = line.substring(nextRow - len, nextRow).reversed()
                val right = line.substring(nextRow, nextRow + len)
                left to right
            }.all { (left, right) -> left == right }
        }.sumOf { it.first + 1 }

        return matrices.sumOf {
            it.first.calc() + it.second.calc() * 100
        }
    }

    fun part2(input: List<String>): Int = TODO()

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day13_test")
    part1(testInput1).println()
    check(part1(testInput1) == 405)

    val input = readInput("Day13")
    part1(input).println()
    check(part1(input) == 28895)

    part2(testInput1).println()
    check(part2(testInput1) == TODO())

    part2(input).println()
    check(part2(input) == TODO())
}
