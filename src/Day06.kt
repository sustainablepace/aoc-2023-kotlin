fun main() {

    fun part1(input: List<String>): Int {
        val (t, d) = input
        val time = t.substring(5).split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val distance = d.substring(9).split(" ").filter { it.isNotBlank() }.map { it.toInt() }

        return time.mapIndexed { index, t ->
            (0..t).count { speed ->
                (t - speed) * speed > distance[index]
            }
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val (t, d) = input
        val time = t.substring(5).split(" ").joinToString("").toLong()
        val distance = d.substring(9).split(" ").joinToString("").toLong()

        return (0L..time).count { speed ->
            (time - speed) * speed > distance
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day06_test")
    part1(testInput1).println()
    check(part1(testInput1) == 288)

    val input = readInput("Day06")
    part1(input).println()
    check(part1(input) == 293046)

    part2(testInput1).println()
    check(part2(testInput1) == 71503)

    part2(input).println()
    check(part2(input) == 35150181)
}
