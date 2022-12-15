package day14

import IOUtil
import kotlin.math.sign

fun main() {
    val inputLines = IOUtil.readResourceFile("input_14.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    val (xLimits, yLimits) = getCoordinateMinMax(inputLines)
    val caveGrid = generateCaveGrid(inputLines, xLimits, yLimits)

    println(howDeepCanTheSandGo(caveGrid, xLimits))

}

private fun howDeepCanTheSandGo(
    caveGrid: Array<Array<Material>>,
    xLimits: Pair<Int, Int>
): Int {
    var sandCount = 0

    // 100% certified clean code
    while (true) {
        var currX = 500 - xLimits.first
        var currY = 0

        var isSandMoving = true
        while (isSandMoving) {
            // Move down
            if (caveGrid[currX][currY + 1] == Material.AIR) {
                currY++
                continue
            }

            // Move down+left
            if (caveGrid[currX - 1][currY + 1] == Material.AIR) {
                currY++
                currX--
                continue
            }

            // Move down+right
            if (caveGrid[currX + 1][currY + 1] == Material.AIR) {
                currY++
                currX++
                continue
            }

            // All blocked -> sand rests
            caveGrid[currX][currY] = Material.SAND
            sandCount++
            if (currX == 500 - xLimits.first && currY == 0) {
                return sandCount
            }
            isSandMoving = false

        }
    }
}

private fun getCoordinateMinMax(inputLines: List<String>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val coordinatePairs = getRockCoordinates(inputLines.joinToString("\n"))

    // Give it some room to breathe
    val xMin = coordinatePairs.minOf { it.first } - 150
    val yMin = coordinatePairs.minOf { it.second }
    val xMax = coordinatePairs.maxOf { it.first } + 150
    // As per assignment description
    val yMax = coordinatePairs.maxOf { it.second } + 2

    return Pair(Pair(xMin, xMax), Pair(yMin, yMax))
}

private fun generateCaveGrid(
    inputLines: List<String>,
    xLimits: Pair<Int, Int>,
    yLimits: Pair<Int, Int>
): Array<Array<Material>> {
    val caveGrid =
        Array(xLimits.second - xLimits.first + 1) {
            Array(yLimits.second + 1) { index -> if (index == yLimits.second) Material.ROCK else Material.AIR }
        }
    for (line in inputLines) {
        val coords = getRockCoordinates(line)

        for (i in 1 until coords.size) {
            val firstPoint = coords[i - 1]
            val secondPoint = coords[i]

            var currX = firstPoint.first
            var currY = firstPoint.second

            while (currX != secondPoint.first || currY != secondPoint.second) {
                caveGrid[currX - xLimits.first][currY] = Material.ROCK

                if (currX != secondPoint.first) {
                    currX += sign((secondPoint.first - currX).toDouble()).toInt()
                }
                if (currY != secondPoint.second) {
                    currY += sign((secondPoint.second - currY).toDouble()).toInt()
                }
            }

            caveGrid[secondPoint.first - xLimits.first][secondPoint.second] = Material.ROCK
        }
    }

    return caveGrid
}

private fun getRockCoordinates(input: String): List<Pair<Int, Int>> {
    return "\\d*,\\d*".toRegex()
        .findAll(input)
        .map {
            val values = it.value.split(",")
            return@map Pair(values[0].toInt(), values[1].toInt())
        }
        .toList()
}
