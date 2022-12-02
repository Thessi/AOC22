package day1

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_1.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    val elves = aggregateAndSumFood(inputLines).sortedDescending()
    println(elves[0] + elves[1] + elves[2])
}

private fun aggregateAndSumFood(input: List<String>): List<Int> {
    var currentElf = 0
    val elves = arrayListOf(0)
    for (entry in input) {
        if (entry.isBlank()) {
            elves.add(0)
            currentElf++
            continue
        }

        elves[currentElf] += entry.toInt()
    }
    return elves
}
