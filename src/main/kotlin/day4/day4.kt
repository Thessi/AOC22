package day4

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_4.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    var overlappingSectionCount = 0
    for (line in inputLines) {
        val (firstElfSections, secondElfSections) = getSectionLimits(line)

        val firstElfRange = firstElfSections.first..firstElfSections.second
        val secondElfRange = secondElfSections.first..secondElfSections.second

        if (firstElfRange.intersect(secondElfRange).isNotEmpty()) {
            overlappingSectionCount++;
        }
    }

    println(overlappingSectionCount)
}

fun getSectionLimits(sectionLine: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val sections = sectionLine.split(",")
    val firstSectionLimits = sections[0].split("-")
    val secondSectionLimits = sections[1].split("-")

    return Pair(
        Pair(firstSectionLimits[0].toInt(), firstSectionLimits[1].toInt()),
        Pair(secondSectionLimits[0].toInt(), secondSectionLimits[1].toInt())
    )
}
