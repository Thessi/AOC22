package day15

import IOUtil

fun main() {
    val inputLines = IOUtil.readResourceFile("input_15.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    val coordinates = parseCoordinates(inputLines)
    val beaconPoint = getSensorMap(coordinates)
    println("${beaconPoint.first}/${beaconPoint.second}: ${beaconPoint.first.toLong() * 4000000L + beaconPoint.second.toLong()}")
}

private fun getSensorMap(coordinates: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Pair<Int, Int> {
    val sensors = coordinates.map { Sensor(it.first.first, it.first.second, it.second.first, it.second.second) }

    for (sensor in sensors) {
        getPointsJustOutsideReach(sensor)
            .filter { it.first in 0..4000000 && it.second in 0..4000000 }
            .forEach {
                if (!isReachableByAnySensor(
                        it,
                        sensors
                    )
                ) return it
            } // Return immediately since we can assume there is only one valid point
    }
    return null!!
}

fun isReachableByAnySensor(point: Pair<Int, Int>, sensors: List<Sensor>): Boolean {
    return sensors.any { it.getDistanceToPoint(point.first, point.second) <= it.beaconDistance }
}

private fun getPointsJustOutsideReach(sensor: Sensor): List<Pair<Int, Int>> {
    val points: ArrayList<Pair<Int, Int>> = ArrayList()

//    var xOffset = 0
//    var yOffset = sensor.beaconDistance + 1

    // top to right
    for (xOffset in 0 until sensor.beaconDistance + 1) {
        points.add(Pair(sensor.x + xOffset, sensor.y + sensor.beaconDistance + 1 - xOffset))
    }
    // right to bottom
    for (xOffset in 0 until sensor.beaconDistance + 1) {
        points.add(Pair(sensor.x + xOffset, sensor.y - sensor.beaconDistance - 1 + xOffset))
    }
    // bottom to left
    for (xOffset in 0 until sensor.beaconDistance + 1) {
        points.add(Pair(sensor.x - xOffset, sensor.y - sensor.beaconDistance - 1 + xOffset))
    }
    // left to top
    for (xOffset in 0 until sensor.beaconDistance + 1) {
        points.add(Pair(sensor.x - xOffset, sensor.y + sensor.beaconDistance + 1 - xOffset))
    }

    return points
}

private fun parseCoordinates(inputLines: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    return inputLines.map {
        "-?\\d+".toRegex()
            .findAll(it)
            .map { it.value.toInt() }
            .toList()
    }.map { Pair(Pair(it[0], it[1]), Pair(it[2], it[3])) }
}
