package day11

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_11.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    val monkeys = parseMonkeys(inputLines)

    println(playAffnRazn(monkeys))
}

fun playAffnRazn(monkeys: ArrayList<Monkey>): Int {
    for (i in 0 until 20) {
        for (monkey in monkeys) {
            while (monkey.items.isNotEmpty()) {
                val itemWorry = monkey.inspectFirstItem() ?: continue
                val targetMonkey = monkey.getTargetMonkey(itemWorry)
                monkeys[targetMonkey].items.add(itemWorry)
            }
        }
    }

    val sortedInteractions = monkeys.map { it.inspectCount }.sortedDescending()

    return sortedInteractions[0] * sortedInteractions[1]
}

fun parseMonkeys(input: List<String>): ArrayList<Monkey> {
    return input.filter { it.isNotEmpty() && !it.startsWith("Monkey") }
        .chunked(5)
        .map { mapToMonkey(it) } as ArrayList<Monkey>
}

private fun mapToMonkey(input: List<String>): Monkey {
    val items = input[0].substring(18)
        .split(", ")
        .map { it.toInt() }

    val operationParts = input[1].substring(19).split(" ")

    val operation = fun(value: Int): Int {
        val operand1 = if (operationParts[0] == "old") value else operationParts[0].toInt()
        val operand2 = if (operationParts[2] == "old") value else operationParts[2].toInt()

        when (operationParts[1]) {
            "+" -> return operand1 + operand2
            "-" -> return operand1 - operand2
            "*" -> return operand1 * operand2
            "/" -> return operand1 / operand2
        }
        return 0
    }

    val testDivisor = input[2].split(" ").last().toInt()
    val testTrueMonkey = input[3].split(" ").last().toInt()
    val testFalseMonkey = input[4].split(" ").last().toInt()

    val test = {value: Int -> if (value % testDivisor == 0) testTrueMonkey else testFalseMonkey }

    return Monkey(operation, test, ArrayDeque(items))
}
