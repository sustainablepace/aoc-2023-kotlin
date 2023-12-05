import kotlin.math.min
class Almanac(input: List<String>, misinterpretation: Boolean = false) {
    private val seeds = input.first().split(": ").last().split(" ").let { block ->
        if(misinterpretation) {
            block.map { it.toLong()..it.toLong() }
        } else {
            block.map { it.toLong() }.chunked(2).map {
                it[0]..it[0] + it[1] - 1
            }
        }
    }
    private val almanacMaps = input.drop(2).joinToString("\n").split("\n\n").map { it.split("\n") }.map {
        it.drop(1).map { line ->
            line.split(" ").map { it.toLong() }
        }.let { numbers ->
            AlmanacMap(
                sourceName = it.first().split(" ").first().split("-").first(),
                destinationName = it.first().split(" ").first().split("-").last(),
                map = numbers.map {
                    AlmanacMapEntry(
                        destination = it[0],
                        source = it[1],
                        range = it[2]
                    )
                }.sortedBy { it.source }
            )
        }
    }

    fun findClosestLocation(): Long {
        var ranges = seeds
        var item = "seed"
        while (item != "location") {
            almanacMaps.first { it.sourceName == item }.let { currentMap ->
                item = currentMap.destinationName
                ranges = currentMap.destinations(ranges)
            }
        }
        return ranges.minOf { it.first }
    }
}

data class AlmanacMap(val sourceName: String, val destinationName: String, val map: List<AlmanacMapEntry>) {
    fun destinations(sourceRanges: List<LongRange>): List<LongRange> {
        return sourceRanges.sortedBy { it.first }.flatMap { range ->
            val destinations = mutableListOf<LongRange>()
            var sweepLine = range.first

            for (currentMap in map) {
                if (sweepLine > range.last) {
                    break
                }
                if (sweepLine > currentMap.sourceRange.last) {
                    continue
                }
                if (sweepLine >= currentMap.sourceRange.first && sweepLine <= currentMap.sourceRange.last) {
                    min(currentMap.sourceRange.last, range.last).let {
                        val delta = currentMap.destination - currentMap.source
                        destinations.add(sweepLine + delta..it + delta)
                        sweepLine = it + 1
                    }
                } else {
                    min(currentMap.sourceRange.first - 1, range.last).let {
                        destinations.add(sweepLine..it)
                        sweepLine = it + 1
                    }
                }
            }
            if (sweepLine <= range.last) {
                destinations.add(sweepLine..range.last)
            }
            destinations
        }
    }
}

data class AlmanacMapEntry(val destination: Long, val source: Long, val range: Long) {
    val sourceRange = source..<source + range
}


fun main() {

    fun part1(input: List<String>): Long = Almanac(input = input, misinterpretation = true).findClosestLocation()
    fun part2(input: List<String>): Long = Almanac(input = input, misinterpretation = false).findClosestLocation()


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
    check(part2(input) == 1928058L)
}
