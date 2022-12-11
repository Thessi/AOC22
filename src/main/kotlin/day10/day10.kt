package day10

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_10.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    println(getScreenOutput(inputLines))
}

private fun getScreenOutput(input: List<String>): String {
    var currSignalStrength = 1
    var currCycle = 0

    val screen = Array(240) { _ -> "."}

    for (command in input) {
        screen[currCycle] = getScreenSymbol(currCycle, currSignalStrength)

        val prevSignalStrength = currSignalStrength
        val commandParts = command.split(" ")
        when (commandParts[0]) {
            "noop" -> currCycle++
            "addx" -> {
                screen[currCycle + 1] = getScreenSymbol(currCycle + 1, prevSignalStrength)
                currCycle += 2
                currSignalStrength += commandParts[1].toInt()
            }
        }
    }

    return screen.toList()
        .chunked(40)
        .joinToString("\n") { it.joinToString("") }
}

fun getScreenSymbol(cycle: Int, signalStrength: Int): String {
    if ((cycle % 40) in (signalStrength - 1)..(signalStrength + 1)) {
        return "#"
    }
    return "."
}
