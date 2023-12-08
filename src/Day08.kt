import kotlin.math.abs

fun main() {

    fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        return abs(a * b) / gcd(a, b)
    }

    fun leastCommonDenominator(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = lcm(result, numbers[i])
        }
        return result
    }

    fun part1(input: List<String>): Int {

        val directions = input.first().toCharArray()
        val nodes = input.drop(2).map { line ->
            line.split(" = ").let { (origin, destinations) ->
                destinations.substring(1, 9).split(", ").let { (left, right) ->
                    origin to mapOf(
                        'L' to left,
                        'R' to right
                    )
                }
            }
        }
        var currentNode = "AAA"
        var index = 0
        var steps = 0
        while (currentNode != "ZZZ") {
            val direction: Char = directions[index]
            currentNode = nodes.first { it.first == currentNode }.second.get(direction)!!
            index = (index + 1) % directions.size
            steps ++
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val directions = input.first().toCharArray()
        val nodes = input.drop(2).map { line ->
            line.split(" = ").let { (origin, destinations) ->
                destinations.substring(1, 9).split(", ").let { (left, right) ->
                    origin to mapOf(
                        'L' to left,
                        'R' to right
                    )
                }
            }
        }.toMap()

        val currentNodes = nodes.filter { it.key.endsWith('A') }
        val q = currentNodes.map { c ->
            var index = 0
            var steps = 0
            var currentNode = c.value
            var currentKey = c.key
            while (!currentKey.endsWith('Z')) {
                val direction: Char = directions[index]
                currentKey = currentNode[direction]!!
                currentNode = nodes[currentKey]!!
                index = (index + 1) % directions.size
                steps ++
            }
            steps
        }
        return leastCommonDenominator(q.map { it.toLong() })
    }

    val testInput1 = readInput("Day08_test")
    val testInput2 = readInput("Day08_test2")
    part1(testInput1).println()
    check(part1(testInput1) == 2)

    val input = readInput("Day08")
    part1(input).println()
    check(part1(input) == 11567)

    part2(testInput2).println()
    check(part2(testInput2) == 6L)

    part2(input).println()
    check(part2(input) == 9858474970153L)
}
