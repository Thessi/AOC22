package day3

import IOUtil

const val itemRanking = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun main() {
    val inputLines = IOUtil.readResourceFile("input_3.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    if (inputLines.size % 3 != 0) {
        println("Yo my code below will break since the line count isn't a multiple of 3. " +
                "Instead of building resilient code, I'll just refuse to process the data. Have a nice day.")
        return
    }

    var totalPriority = 0
    var lineCounter = 0
    while (lineCounter < inputLines.size) {
        val duplicateItems = getDuplicateItems(inputLines[lineCounter].toSet(),
            inputLines[lineCounter + 1].toSet(),
            inputLines[lineCounter + 2].toSet())

        totalPriority += calcPriority(duplicateItems)
        lineCounter += 3
    }
    println(totalPriority)
}

fun calcPriority(duplicateItems: Set<Char>): Int {
    return duplicateItems.sumOf { itemRanking.indexOf(it) + 1 }
}

fun getDuplicateItems(rucksackOne: Set<Char>, rucksackTwo: Set<Char>, rucksackThree: Set<Char>): Set<Char> {
    return rucksackOne
        .intersect(rucksackTwo.toSet())
        .intersect(rucksackThree.toSet())
}
