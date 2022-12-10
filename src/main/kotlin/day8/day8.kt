package day8

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_8.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    println(countVisibleTrees(inputLines))
}

fun countVisibleTrees(input: List<String>): Int {
    val visibleTrees = HashSet<String>() // Just save coordinates as "x/y" cause idc

    val topToBottomMax = Array(input.size) { _ -> -1 }

    val bottomToTopCurrVisible = Array(input.size) { _ -> HashMap<String, Int>() }
    input.forEachIndexed { rowNumber, treeRow ->
        var leftToRightMax = -1

        var rightToLeftCurrVisible = HashMap<String, Int>()
        treeRow.forEachIndexed { columnNumber, tree ->
            val treeHeight = tree.digitToInt()
            if (treeHeight > leftToRightMax) {
                visibleTrees.add("$rowNumber/$columnNumber")
                leftToRightMax = treeHeight
            }

            rightToLeftCurrVisible = rightToLeftCurrVisible.filter { (_, height) -> height > treeHeight } as HashMap
            rightToLeftCurrVisible["$rowNumber/$columnNumber"] = treeHeight

            if (treeHeight > topToBottomMax[columnNumber]) {
                visibleTrees.add("$rowNumber/$columnNumber")
                topToBottomMax[columnNumber] = treeHeight
            }

            bottomToTopCurrVisible[columnNumber] = bottomToTopCurrVisible[columnNumber].filter { (_, height) -> height > treeHeight } as HashMap
            bottomToTopCurrVisible[columnNumber]["$rowNumber/$columnNumber"] = treeHeight

        }
        visibleTrees.addAll(rightToLeftCurrVisible.keys)
    }

    visibleTrees.addAll(bottomToTopCurrVisible.flatMap { it.keys })

    return visibleTrees.size
}
