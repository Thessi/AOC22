package day9

import IOUtil
import kotlin.math.abs

fun main() {
    val inputLines = IOUtil.readResourceFile("input_9.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    println(getVisitedTailPositionCount(inputLines))
}

private fun getVisitedTailPositionCount(input: List<String>): Int {
    val visitedPositions = hashSetOf("0/0") // Just save coordinates as "x/y" cause idc

    val knots = Array(10) { _ -> Pair(0, 0)}

    for (move in input) {
        val (direction, steps) = parseMove(move)

        for (i in 0 until steps) {
            // Apparently you can't destructure into already declared variables, the more you know..
            val (newHeadX, newHeadY) = getNewHeadPosition(knots[0].first, knots[0].second, direction)
            knots[0] = Pair(newHeadX, newHeadY)

            for (knotIndex in 1 until knots.size) {
                val knot = knots[knotIndex]
                val currentHead = knots[knotIndex - 1]
                val (newTailX, newTailY) = getNewTailPosition(knot.first, knot.second, currentHead.first, currentHead.second)
                knots[knotIndex] = Pair(newTailX, newTailY)
            }

            visitedPositions.add("" + knots[9].first + "/" + knots[9].second)
        }

    }

    return visitedPositions.size
}

fun getNewTailPosition(tailX: Int, tailY: Int, headX: Int, headY: Int): Pair<Int, Int> {
    val xGap = headX - tailX
    val yGap = headY - tailY

    // Next to each other -> do nothing
    if (abs(xGap) <= 1 && abs(yGap) <= 1) {
        return Pair(tailX, tailY)
    }

    var tailXStep = 0
    var tailYStep = 0
    // Horizontal move
    if (yGap == 0) {
        tailXStep = xGap / abs(xGap)
    }

    // Vertical move
    if (xGap == 0) {
        tailYStep = yGap / abs(yGap)
    }

    // Diagonal move
    if (abs(xGap) + abs(yGap) > 2) {
        tailXStep = xGap / abs(xGap)
        tailYStep = yGap / abs(yGap)
    }

    return Pair(tailX + tailXStep, tailY + tailYStep)
}

private fun getNewHeadPosition(headX: Int, headY: Int, direction: String): Pair<Int, Int> {
    when (direction) {
        "U" -> return Pair(headX, headY + 1)
        "R" -> return Pair(headX + 1, headY)
        "D" -> return Pair(headX, headY - 1)
        "L" -> return Pair(headX - 1, headY)
    }

    return Pair(headX, headY)
}

private fun parseMove(input: String): Pair<String, Int> {
    val moveParts = input.split(" ")
    return Pair(moveParts[0], moveParts[1].toInt())
}
