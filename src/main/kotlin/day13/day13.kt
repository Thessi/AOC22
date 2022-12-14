package day13

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_13.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    (inputLines as ArrayList).addAll(listOf("[[2]]", "[[6]]"))

    getCorrectOrderIndexSum(inputLines.filter { it != "" })
}

fun getCorrectOrderIndexSum(input: List<String>) {
    val sorted = input.map { parseInput(it) }
        .sorted()

    println((sorted.indexOfFirst { it.isMarker } + 1) * (sorted.indexOfLast { it.isMarker } + 1))
}

fun parseInput(input: String): PacketValue {
    // Makes parsing easier, since "10" is the only two-char symbol
    val processedInput = input.substring(1, input.length - 1).replace("10", "a")

    val firstPacketValue = PacketValue(null)

    if (input == "[[2]]" || input == "[[6]]") {
        firstPacketValue.isMarker = true
    }

    var currPacketValue = firstPacketValue
    for (symbol in processedInput) {
        when (symbol) {
            '[' -> {
                val newPacketValue = PacketValue(currPacketValue)
                currPacketValue.list.add(newPacketValue)
                currPacketValue = newPacketValue
            }
            ']' -> currPacketValue = currPacketValue.parent ?: currPacketValue
        }

        var value: Int? = null
        if (symbol == 'a') {
            value = 10
        }
        if (symbol.isDigit()) {
            value = symbol.digitToInt()
        }

        if (value != null) {
            currPacketValue.list.add(PacketValue(currPacketValue, value))
        }
    }

    return firstPacketValue
}
