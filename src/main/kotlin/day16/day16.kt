package day16

import IOUtil
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

// 29 since 30 didn't work, off by one is the programmers bane
const val time: Int = 26
var upperBound: Int = Int.MIN_VALUE

fun main() {
    val inputLines = IOUtil.readResourceFile("input_16.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    val rooms = parseInput(inputLines)
    findBestPath(rooms)
    println(upperBound)
}

private fun findBestPath(rooms: List<CaveRoom>) {
    val sortedValves = rooms.map { Pair(it.valveLabel, it.flowRate) }.sortedByDescending { it.second }
    val startRoom = rooms.find { it.valveLabel == "AA" }!!

    walkThePath(startRoom, startRoom, sortedValves, time, ArrayList(), 0)
}

private fun walkThePath(meRoom: CaveRoom, elephantRoom: CaveRoom, sortedValves: List<Pair<String, Int>>, minutesRemaining: Int,
                        turnedOnValves: ArrayList<String>, releasePressure: Int) {
    if (minutesRemaining < 0) {
        upperBound = max(upperBound, releasePressure)
        return
    }

    if (getBestPossibleValue(releasePressure, turnedOnValves, sortedValves, minutesRemaining) <= upperBound) {
        return
    }

    // turn on both valves
    if (meRoom.flowRate > 0 && !turnedOnValves.contains(meRoom.valveLabel) &&
        elephantRoom.flowRate > 0 && !turnedOnValves.contains(elephantRoom.valveLabel) &&
        meRoom.valveLabel != elephantRoom.valveLabel) {
            val newTurnedOnValves = turnedOnValves.toMutableList() as ArrayList
            newTurnedOnValves.add(meRoom.valveLabel)
            newTurnedOnValves.add(elephantRoom.valveLabel)
            walkThePath(meRoom, elephantRoom, sortedValves, minutesRemaining - 1, newTurnedOnValves,
                releasePressure + (meRoom.flowRate + elephantRoom.flowRate) * (minutesRemaining - 1))
    }

    // turn on valve in meRoom and move elephant
    if (meRoom.flowRate > 0 && !turnedOnValves.contains(meRoom.valveLabel)) {
        elephantRoom.connectedRooms.forEach {
            val newTurnedOnValves = turnedOnValves.toMutableList() as ArrayList
            newTurnedOnValves.add(meRoom.valveLabel)
            walkThePath(meRoom, it, sortedValves, minutesRemaining - 1, newTurnedOnValves,
                releasePressure + meRoom.flowRate * (minutesRemaining - 1))
        }
    }

    // turn on valve in elephantRoom and move me
    if (elephantRoom.flowRate > 0 && !turnedOnValves.contains(elephantRoom.valveLabel)) {
        meRoom.connectedRooms.forEach {
            val newTurnedOnValves = turnedOnValves.toMutableList() as ArrayList
            newTurnedOnValves.add(elephantRoom.valveLabel)
            walkThePath(it, elephantRoom, sortedValves, minutesRemaining - 1, newTurnedOnValves,
                releasePressure + elephantRoom.flowRate * (minutesRemaining - 1))
        }
    }

    // move both to next room
    meRoom.connectedRooms.forEach { nextMeRoom ->
        elephantRoom.connectedRooms.forEach { nextElephantRoom ->
            walkThePath(nextMeRoom, nextElephantRoom, sortedValves, minutesRemaining - 1,
                turnedOnValves, releasePressure)
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

