package day7

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_7.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    val root = getFileTree(inputLines)

    val freeSpace = 70000000 - root.getTotalSize()

    println(getFolderSizesBiggerThan(root, 30000000 - freeSpace).min())
}

private fun getFileTree(inputLines: List<String>): AocDirectory {
    val root = AocDirectory("/")
    var currentDir = root
    for (line in inputLines) {
        if (line == "\$ cd /") {
            continue
        }
        val commands = line.split(" ")

        if (commands[0] == "$") {
            if (commands[1] == "cd") {
                if (commands[2] == "..") {
                    currentDir = currentDir.parent!!
                } else {
                    currentDir = currentDir.getSubdirectory(commands[2])
                }
            }
        } else if (commands[0].all { it.isDigit() }) {
            currentDir.addFile(commands[1], commands[0].toLong())
        }
    }

    return root
}

private fun getFolderSizesBiggerThan(dir: AocDirectory, size: Long): List<Long> {
    val validSizes = ArrayList<Long>()
    if (dir.getTotalSize() >= size) {
        validSizes.add(dir.getTotalSize())
    }

    validSizes.addAll(dir.directories.map { getFolderSizesBiggerThan(it, size) }.flatMap { it.toList() })
    return validSizes
}
