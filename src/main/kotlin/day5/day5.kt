package day5

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_5.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }
    val (stackInput, stackCount, moveInput) = splitInputIntoStacksAndMoves(inputLines)

    val stacks = constructCrateStacks(stackInput, stackCount)
    val movedStacks = applyMoves(stacks, moveInput)

    println(movedStacks.joinToString(""))
}

fun applyMoves(stacks: Array<ArrayDeque<Char>>, moveInput: List<String>): List<Char> {
    val moves = moveInput.stream()
        .map { it.replace("(move |from |to )".toRegex(), "").split(" ") }
        .toList()
    for (move in moves) {
        val moveCount = move[0].toInt()
        val moveSource = move[1].toInt() - 1
        val moveDest = move[2].toInt() - 1

        val tempStack = ArrayDeque<Char>()
        for (i in 0 until moveCount) {
            val crate = stacks[moveSource].removeLast()
            tempStack.add(crate)
        }

        while (tempStack.isNotEmpty()) {
            stacks[moveDest].add(tempStack.removeLast())
        }
    }

    return stacks.toList().stream().map { it.last() }.toList()
}

fun splitInputIntoStacksAndMoves(inputLines: List<String>): Triple<List<String>, Int, List<String>> {
    val stacks = ArrayList<String>()

    var lineIndex = 0
    while (inputLines[lineIndex].startsWith("[")) {
        stacks.add(inputLines[lineIndex])
        lineIndex++
    }

    val stackCount = inputLines[lineIndex]
        .trim()
        .split("   ")
        .stream()
        .map { it.toInt() }
        .toList()
        .max()

    return Triple(stacks, stackCount, inputLines.subList(lineIndex + 2, inputLines.size))
}

fun constructCrateStacks(input: List<String>, stackCount: Int): Array<ArrayDeque<Char>> {
    val stacks = Array(stackCount) { _ -> ArrayDeque<Char>()}

    for (line in input.reversed()) {
        line.forEachIndexed { index, letter ->
            if (letter !in charArrayOf('[', ']', ' ')) {
                stacks[index / 4].add(letter)
            }
        }
    }

    return stacks
}
