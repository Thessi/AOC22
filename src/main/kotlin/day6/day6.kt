package day6

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_6.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    val signal = inputLines[0]
    for (i in 0..(signal.length - 14)) {
        if (signal.substring(i, i + 14).toCharArray().distinct().size == 14) {
            println(i + 14)
            return
        }
    }
}
