package day16

import IOUtil
import java.time.LocalDateTime
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

// 29 since 30 didn't work, off by one is the programmers bane
const val time: Int = 29
var upperBound: Int = Int.MIN_VALUE

fun main() {
    val inputLines = IOUtil.readResourceFile("input_16.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    println(LocalDateTime.now())
    val rooms = parseInput(inputLines)
    findBestPath(rooms)
    println(LocalDateTime.now())
    println(upperBound)
}

private fun findBestPath(rooms: List<CaveRoom>) {
    val sortedValves = rooms.map { Pair(it.valveLabel, it.flowRate) }.sortedByDescending { it.second }
    val startRoom = rooms.find { it.valveLabel == "AA" }!!

    walkThePath(startRoom, sortedValves, time, ArrayList(), 0)
}

private fun walkThePath(room: CaveRoom, sortedValves: List<Pair<String, Int>>, minutesRemaining: Int,
                        turnedOnValves: ArrayList<String>, releasePressure: Int) {
    if (minutesRemaining < 0) {
        upperBound = max(upperBound, releasePressure)
        return
    }

    if (getBestPossibleValue(releasePressure, turnedOnValves, sortedValves, minutesRemaining) < upperBound) {
        return
    }

    room.connectedRooms.forEach {
        // continue without turning on valve
        walkThePath(it, sortedValves, minutesRemaining - 1, turnedOnValves, releasePressure)

        // continue with turning on valve
        if (it.flowRate > 0 && !turnedOnValves.contains(it.valveLabel)) {
            val newTurnedOnValves = turnedOnValves.toMutableList() as ArrayList
            newTurnedOnValves.add(it.valveLabel)
            walkThePath(it, sortedValves, minutesRemaining - 2, newTurnedOnValves,
                releasePressure + it.flowRate * (minutesRemaining - 1))
        }
    }
}

fun getBestPossibleValue(releasePressure: Int, turnedOnValves: List<String>, sortedValves: List<Pair<String, Int>>,
                         minutesRemaining: Int): Int {
    // Best possible value = path that takes the highest pressure valves which are still available in descending order
    val notYetTurnedOnValves = sortedValves.filter { !turnedOnValves.contains(it.first) }
    return releasePressure + notYetTurnedOnValves
        .subList(0, min(ceil((minutesRemaining - 1) / 2.0).toInt(), notYetTurnedOnValves.size))
        .mapIndexed { index, valve -> valve.second * (minutesRemaining - index * 2) }
        .sum()
}

private fun parseInput(inputLines: List<String>): List<CaveRoom> {
    val rooms = ArrayList<CaveRoom>()
    for (inputLine in inputLines) {
        val roomSplit = inputLine.split(" ")
        val valveLabel = roomSplit[1]

        var room = rooms.find { it.valveLabel == valveLabel }
        if (room == null) {
            room = CaveRoom(valveLabel)
            rooms.add(room)
        }

        room.flowRate = roomSplit[4].split("=")[1].replace(";", "").toInt()

        room.connectedRooms = roomSplit.subList(9, roomSplit.size).map { label ->
            val processedLabel = label.replace(",", "")
            var foundRoom = rooms.find { it.valveLabel == processedLabel }
            if (foundRoom == null) {
                foundRoom = CaveRoom(processedLabel)
                rooms.add(foundRoom)
            }
            return@map foundRoom
        }
    }

    return rooms
}

