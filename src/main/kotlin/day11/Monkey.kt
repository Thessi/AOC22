package day11

data class Monkey(
    val operation: (Map<Int, Long>) -> Map<Int, Long>,
    val testValue: Int,
    val testTrueMonkey: Int,
    val testFalseMonkey: Int,
    val items: ArrayDeque<Map<Int, Long>> = ArrayDeque(),
    var inspectCount: Long = 0
) {
    fun inspectFirstItem(): Map<Int, Long>? {
        if (items.isEmpty()) {
            return null
        }
        inspectCount++
        return operation(items.removeFirst())
    }

    fun getTargetMonkey(itemWorry: Map<Int, Long>): Int {
        return if (itemWorry[testValue] == 0L) testTrueMonkey else testFalseMonkey
    }
}
