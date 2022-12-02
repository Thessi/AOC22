package day2

enum class Shape(val pointValue: Int) {
    UNKNOWN(0),
    ROCK(1),
    PAPER(2),
    SCISSOR(3);

    companion object {
        private val map = Shape.values().associateBy(Shape::pointValue)
        fun byPointValue(type: Int) = map[type]
    }

    fun getResultAgainst(opponentShape: Shape): RoundResult {
        if (this == UNKNOWN || opponentShape == UNKNOWN) {
            return RoundResult.UNKNOWN
        }
        if (this == opponentShape) {
            return RoundResult.DRAW
        }

        // hacky reuse of pointValue
        if ((this.pointValue + 1) % 3 == opponentShape.pointValue % 3) {
            return RoundResult.LOSS
        }

        return RoundResult.WIN
    }

    fun getWinShape(): Shape {
        return Shape.byPointValue(this.pointValue % 3 + 1)!!
    }

    fun getLossShape(): Shape {
        val winValue = (this.pointValue - 1) % 3

        return Shape.byPointValue(if (winValue == 0) 3 else winValue)!!
    }
}
