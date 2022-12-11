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

fun playAffnRazn(monkeys: ArrayList<Monkey>): Long {
    for (i in 0 until 10000) {
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
    val testDivisors = input.filter { it.startsWith("  Test:") }
        .map { it.split(" ").last().toInt() }

    return input.filter { it.isNotEmpty() && !it.startsWith("Monkey") }
        .chunked(5)
        .map { mapToMonkey(it, testDivisors) } as ArrayList<Monkey>
}

private fun mapToMonkey(input: List<String>, testDivisors: List<Int>): Monkey {
    val itemWorries = input[0].substring(18)
        .split(", ")
        .map { it.toLong() }

    val items = itemWorries
        .map { worry ->
            testDivisors.map { it to worry % it }
                .associate { it }
        }


    val operationParts = input[1].substring(19).split(" ")

    val operation = fun(divisorRemainders: Map<Int, Long>): Map<Int, Long> {
        val newDivisorRemainders = divisorRemainders as HashMap
        for (key in newDivisorRemainders.keys) {
            if (!newDivisorRemainders.containsKey(key)) {
                continue
            }
            val value = newDivisorRemainders[key]
            val operand1 = if (operationParts[0] == "old") value!! else operationParts[0].toLong()
            val operand2 = if (operationParts[2] == "old") value!! else operationParts[2].toLong()

            var operationResult = 0L
            when (operationParts[1]) {
                "+" -> operationResult = operand1.plus(operand2)
                "*" -> operationResult = operand1.times(operand2)
            }

            newDivisorRemainders[key] = operationResult % key
        }

        return newDivisorRemainders
    }

    val testDivisor = input[2].split(" ").last().toInt()
    val testTrueMonkey = input[3].split(" ").last().toInt()
    val testFalseMonkey = input[4].split(" ").last().toInt()

    return Monkey(operation, testDivisor, testTrueMonkey, testFalseMonkey, ArrayDeque(items))
}
