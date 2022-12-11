package day10

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_10.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    println(calcSignalStrengthSum(inputLines))
}

private fun calcSignalStrengthSum(input: List<String>): Int {
    var signalStrengthSum = 0
    var currSignalStrength = 1
    var currCycle = 1

    for (command in input) {
        val prevSignalStrength = currSignalStrength
        val commandParts = command.split(" ")
        when (commandParts[0]) {
            "noop" -> currCycle++
            "addx" -> {
                currCycle += 2
                currSignalStrength += commandParts[1].toInt()
            }
        }

        // Command finished on milestone cycle -> take new value
        if (currCycle <= 221 && (currCycle - 20) % 40 == 0) {
            signalStrengthSum += currSignalStrength * currCycle
        }

        // Command finished after milestone cycle -> take prev value
        // If command was "noop", signal was already counted on prev cycle -> skip
        if (currCycle <= 221 && (currCycle - 20) % 40 == 1 && commandParts[0] != "noop") {
            signalStrengthSum += prevSignalStrength * (currCycle - 1)
        }
    }

    return signalStrengthSum
}
