package day15

import kotlin.math.abs

data class Sensor(val x: Int, val y: Int, val beaconDistance: Int) {
    constructor(sensorX: Int, sensorY: Int, beaconX: Int, beaconY: Int): this(sensorX, sensorY, abs(sensorX - beaconX) + abs(sensorY - beaconY)) {

    }

    fun getDistanceToPoint(oX: Int, oY: Int): Int {
        return abs(x - oX) + abs(y - oY)
    }
}
