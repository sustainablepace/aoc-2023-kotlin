import java.util.*

data class Almanac(val sourceName: String, val destinationName: String, val map: List<AlmanacMap>)
data class AlmanacMap(val destination: Long, val source: Long, val range: Long)

fun main() {

    fun part1(input: List<String>): Long {
        val seeds = input.first().split(": ").last().split(" ").map { it.toLong() }
        val almanacLists = input.drop(2).joinToString("\n").split("\n\n").map { it.split("\n") }.map {
            it.drop(1).map { line ->
                line.split(" ").map { it.toLong() }
            }.let { numbers ->
                Almanac(
                    sourceName = it.first().split(" ").first().split("-").first(),
                    destinationName = it.first().split(" ").first().split("-").last(),
                    map = numbers.map {
                        AlmanacMap(
                            destination = it[0],
                            source = it[1],
                            range = it[2]
                        )
                    }
                )
            }
        }

        return seeds.minOf { seed ->
            var item = "seed"
            var number = seed
            while (item != "location") {
                almanacLists.first { it.sourceName == item }.let { almanac ->
                    item = almanac.destinationName
                    number = almanac.map.firstOrNull { map ->
                        number >= map.source && number <= map.source + map.range
                    }?.let { map ->
                        val index = number - map.source
                        map.destination + index
                    } ?: number
                }
            }
            number
        }
    }

    fun part2(input: List<String>): Long = TODO()


// test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day05_test")
    part1(testInput1).println()
    check(part1(testInput1) == 35L)

    val input = readInput("Day05")
    part1(input).println()
    check(part1(input) == 165788812L)

    part2(testInput1).println()
    check(part2(testInput1) == 46L)

    part2(input).println()
    check(part2(input) == TODO())
}
