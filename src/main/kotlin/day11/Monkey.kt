package day11

data class Monkey(val operation: (Int) -> Int,
                  val test: (Int) -> Int,
                  val items: ArrayDeque<Int> = ArrayDeque(),
                  var inspectCount: Int = 0) {
    fun inspectFirstItem(): Int? {
        if (items.isEmpty()) {
            return null
        }
        inspectCount++
        val item = items.removeFirst()
        return Math.floorDiv(operation(item), 3)
    }

    fun getTargetMonkey(itemWorry: Int): Int {
        return test(itemWorry)
    }
}
