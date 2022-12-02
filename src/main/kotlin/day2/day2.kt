package day2

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_2.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    println(inputLines.sumOf { calcRoundResult(it) })
}

private fun calcRoundResult(roundInput: String): Int {
    val shapes = roundInput.split(" ")

    if (shapes.size != 2) {
        println("Incorrect input: $roundInput")
        return 0
    }

    val opponentShape = getOpponentShape(shapes[0][0])
    val playerShape = getPlayerShape(shapes[1][0], opponentShape)

    var score = playerShape.pointValue

    when (playerShape.getResultAgainst(opponentShape)) {
        RoundResult.DRAW -> score += 3
        RoundResult.WIN -> score += 6
        else -> null
    }

    return score
}

private fun getOpponentShape(input: Char): Shape {
    return when (input) {
        'A' -> Shape.ROCK
        'B' -> Shape.PAPER
        'C' -> Shape.SCISSOR
        else -> {
            println("Invalid shape input: $input")
            Shape.UNKNOWN
        }
    }
}

private fun getPlayerShape(playerInput: Char, opponentShape: Shape): Shape {
    return when (playerInput) {
        'X' -> opponentShape.getLossShape()
        'Y' -> opponentShape
        'Z' -> opponentShape.getWinShape()
        else -> {
            println("Invalid shape input: $playerInput")
            Shape.UNKNOWN
        }
    }
}
