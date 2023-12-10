import Direction.*

enum class Direction {
    N, E, S, W
}

fun main() {

    fun Char.isCompatibleTo(direction: Direction, other: Char): Boolean {
        return mapOf(
            ('S' to N) to setOf('|', '7', 'F'),
            ('S' to E) to setOf('-', 'J', '7'),
            ('S' to W) to setOf('-', 'L', 'F'),
            ('S' to S) to setOf('|', '-', 'L', 'J'),
            ('|' to N) to setOf('|', '7', 'F', 'S'),
            ('|' to S) to setOf('|', 'L', 'J', 'S'),
            ('-' to E) to setOf('-', 'J', '7', 'S'),
            ('-' to W) to setOf('-', 'L', 'F', 'S'),
            ('L' to N) to setOf('|', '7', 'F', 'S'),
            ('L' to E) to setOf('-', '7', 'J', 'S'),
            ('7' to W) to setOf('-', 'L', 'F', 'S'),
            ('7' to S) to setOf('|', 'L', 'J', 'S'),
            ('J' to N) to setOf('|', '7', 'F', 'S'),
            ('J' to W) to setOf('-', 'L', 'F', 'S'),
            ('F' to E) to setOf('-', 'J', '7', 'S'),
            ('F' to S) to setOf('|', 'L', 'J', 'S')
        )[this to direction]?.contains(other) ?: false
    }

    fun List<String>.startingPoint() = mapIndexedNotNull { index, row ->
        row.indexOfFirst { it == 'S' }.let { column ->
            if (column == -1) {
                null
            } else {
                column to index
            }
        }
    }.first()

    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = (first + other.first) to (second + other.second)

    fun List<String>.contains(p: Pair<Int, Int>): Boolean {
        return (0..<first().length).contains(p.first) && indices.contains(p.second)
    }

    fun List<String>.symbolAt(n: Pair<Int, Int>) = get(n.second)[n.first]


    fun List<String>.neighboursOf(
        s: Pair<Int, Int>
    ) = mapOf(
        S to (0 to 1),
        N to (0 to -1),
        W to (-1 to 0),
        E to (1 to 0)
    ).mapNotNull { (k, v) ->
        (v + s).let { n ->
            if (this.contains(n)) {
                k to n
            } else {
                null
            }
        }
    }.filter { (k, v) ->
        symbolAt(s).isCompatibleTo(k, symbolAt(v))
    }.map {
        it.second
    }.toSet()


    fun List<String>.nextAfter(
        s: Pair<Int, Int>
    ) = mapOf(
        S to (0 to 1),
        N to (0 to -1),
        W to (-1 to 0),
        E to (1 to 0)
    ).mapNotNull { (k, v) ->
        (v + s).let { n ->
            if (this.contains(n)) {
                k to n
            } else {
                null
            }
        }
    }.filter { (k, v) ->
        symbolAt(s).isCompatibleTo(k, symbolAt(v))
    }.map {
        it
    }.toSet()

    fun part1(input: List<String>): Int {
        val s = input.startingPoint()
        var options = mutableSetOf(s)
        val checked = mutableSetOf<Pair<Int, Int>>(s)
        var steps = 0
        while (options.isNotEmpty()) {
            val n = options.flatMap<Pair<Int, Int>, Pair<Int, Int>> { p ->
                input.neighboursOf(p)
            }.toSet() - checked
            checked.addAll(n)
            options = n.toMutableSet()
            steps++
        }
        return steps - 1
    }


    fun part2(input: List<String>): Int {
        val s = input.startingPoint()
        var next: Pair<Int, Int> = s
        val checked = mutableSetOf<Pair<Int, Int>>(s)
        val loop = mutableListOf<Pair<Direction, Pair<Int, Int>>>()
        do {
            val nextStep = input.nextAfter(next).firstOrNull { !checked.contains(it.second)}
            if (nextStep != null) {
                next = nextStep.second
                checked.add(next)
                loop.add(nextStep)
            }
        } while (nextStep != null)

        val orientation = loop.mapNotNull {
            mapOf(
                (S to 'J') to "right",
                (S to 'L') to "left",
                (E to '7') to "right",
                (E to 'J') to "left",
                (N to 'F') to "right",
                (N to '7') to "left",
                (W to 'L') to "right",
                (W to 'F') to "left",
            )[it.first to input.symbolAt(it.second)]
        }.partition { it == "left" }.let {
            if (it.first.size > it.second.size) {
                "left"
            } else {
                "right"
            }
        }

        val possibleEnclosures = loop.flatMap {
            mapOf(
                Triple("right", S, '|') to setOf(-1 to 0),
                Triple("left", S, '|') to setOf(1 to 0),
                Triple("right", S, 'L') to setOf(-1 to 0, 0 to 1),
                Triple("left", S, 'J') to setOf(1 to 0, 0 to 1),
                Triple("right", N, '|') to setOf(1 to 0),
                Triple("left", N, '|') to setOf(-1 to 0),
                Triple("right", N, '7') to setOf(1 to 0, 0 to -1),
                Triple("left", N, 'F') to setOf(-1 to 0, 0 to -1),
                Triple("right", W, '-') to setOf(0 to -1),
                Triple("left", W, '-') to setOf(0 to 1),
                Triple("right", W, 'F') to setOf(0 to 1, -1 to 0),
                Triple("left", W, 'L') to setOf(0 to -1, -1 to 0),
                Triple("right", E, '-') to setOf(0 to 1),
                Triple("left", E, '-') to setOf(0 to -1),
                Triple("right", E, 'J') to setOf(0 to -1, 1 to 0),
                Triple("left", E, '7') to setOf(0 to 1, 1 to 0)
            )[Triple(orientation, it.first, input.symbolAt(it.second))]?.map {p ->
                p + it.second
            } ?: emptySet()
        }.toSet() - checked

        var new = possibleEnclosures
        do {
            val old = new
            new = old.union(old.flatMap<Pair<Int, Int>, Pair<Int, Int>> { p ->
                setOf(
                    0 to 1,
                    0 to -1,
                    -1 to 0,
                    1 to 0
                ).map<Pair<Int, Int>, Pair<Int, Int>> { n ->
                    (n + p)
                }.filter {
                    input.contains(it)
                }
            }) - checked
        } while (new != old)
        return new.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day10_test")
    val testInput2 = readInput("Day10_test2")
    val testInput3 = readInput("Day10_test3")
    val testInput4 = readInput("Day10_test4")
    part1(testInput1).println()
    check(part1(testInput1) == 4)

    part1(testInput2).println()
    check(part1(testInput2) == 8)

    val input = readInput("Day10")
    part1(input).println()
    check(part1(input) == 6733)

    part2(testInput1).println()
    check(part2(testInput1) == 1)

    part2(testInput2).println()
    check(part2(testInput2) == 1)

    part2(testInput3).println()
    check(part2(testInput3) == 4)

    part2(testInput4).println()
    check(part2(testInput4) == 8)

    part2(input).println()
    check(part2(input) == 435)
}


