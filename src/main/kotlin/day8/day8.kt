package day8

import IOUtil
import kotlin.math.max

fun main() {
    val inputLines = IOUtil.readResourceFile("input_8.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    println(getHighestScenicScore(inputLines))
}

fun getHighestScenicScore(input: List<String>): Int {
    var highScore = 0

    val columnIndexedInput = rotateForest(input)

    input.forEachIndexed { rowNumber, treeRow ->
        treeRow.forEachIndexed { columnNumber, tree ->
            val treeHeight = tree.digitToInt()
            var score = getViewDistanceLeft(input[rowNumber], columnNumber, treeHeight) // L -> R
            score *= getViewDistanceRight(input[rowNumber], columnNumber, treeHeight) // R -> L
            score *= getViewDistanceLeft(columnIndexedInput[columnNumber], rowNumber, treeHeight) // B -> T
            score *= getViewDistanceRight(columnIndexedInput[columnNumber], rowNumber, treeHeight) // T -> B

            highScore = max(score, highScore)
        }
    }

    return highScore
}

fun rotateForest(input: List<String>): List<String> {
    val rotatedInput = Array(input[0].length) { _ -> "" }

    for (treeRow in input) {
        treeRow.forEachIndexed { columnNumber, tree ->
            rotatedInput[columnNumber] = rotatedInput[columnNumber] + tree
        }
    }

    return rotatedInput.toList()
}

fun getViewDistanceLeft(treeRow: String, columnNumber: Int, treeHouseHeight: Int): Int {
    if (columnNumber == 0) {
        return 0
    }
    return getViewDistance(treeRow.substring(0, columnNumber).reversed(), treeHouseHeight)
}

fun getViewDistanceRight(treeRow: String, columnNumber: Int, treeHouseHeight: Int): Int {
    if (columnNumber == treeRow.length + 1) {
        return 0
    }
    return getViewDistance(treeRow.substring(columnNumber + 1), treeHouseHeight)
}

private fun getViewDistance(treeRow: String, treeHouseHeight: Int): Int {
    var distance = 0
    for (treeHeight in treeRow) {
        distance++
        if (treeHeight.digitToInt() >= treeHouseHeight) {
            break
        }
    }
    return distance

}
