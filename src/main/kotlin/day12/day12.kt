package day12

import IOUtil
import kotlin.math.min

fun main() {
    val inputLines = IOUtil.readResourceFile("input_12.txt")
    if (inputLines == null) {
        println("no data :(")
        return
    }

    println(
        getStartPointMarkedInputs(inputLines)
            .map { parseElevations(it) }
            .map { doTheDjikstra(it) }
            .filter { it > 0 }
            .min()
    )
}

private fun getStartPointMarkedInputs(input: List<String>): List<List<String>> {
    (input as ArrayList).forEachIndexed { i, row -> input[i] = row.replace('S', 'a') }
    val maps = ArrayList<ArrayList<String>>()
    input.forEachIndexed { rowNumber, row ->
        row.forEachIndexed { colNumber, point ->
            if (point == 'a') {
                val listCopy = ArrayList(input)
                listCopy[rowNumber] = listCopy[rowNumber].substring(0, colNumber) + 'S' + listCopy[rowNumber].substring(colNumber + 1)
                maps.add(listCopy)
            }
        }
    }

    return maps
}

fun doTheDjikstra(startPoint: ElevationPoint): Int {
    val nodesToProcess = ArrayDeque<ElevationPoint>()
    nodesToProcess.add(startPoint)

    while (nodesToProcess.isNotEmpty()) {

        val currNode = nodesToProcess.removeFirst()
        currNode.visited = true

        currNode.neighbors.forEach {
            it.distance = min(it.distance, currNode.distance + 1)
            if (!it.visited && nodesToProcess.find { node -> node.location == it.location } == null) {
                nodesToProcess.add(it)
            }

            if (it.isDestination) {
                return it.distance
            }
        }
    }

    return 0
}

private fun parseElevations(input: List<String>): ElevationPoint {
    val elevationMatrix = Array(input.size) { Array(input[0].length) { ElevationPoint() } }

    // Just init it now so the compiler doesn't complain about returning a non-initialized variable
    // The input will always include an "S" so it will always be set to a proper value (source just trust me bro)
    var startPoint = ElevationPoint()

    input.forEachIndexed { rowNumber, elevationLine ->
        elevationLine.forEachIndexed { columnNumber, elevation ->
            val currElevationPoint = elevationMatrix[rowNumber][columnNumber]
            currElevationPoint.isDestination = elevation == 'E'
            currElevationPoint.neighbors.addAll(getReachableNeighbors(input, rowNumber, columnNumber, elevationMatrix))
            currElevationPoint.location = "$rowNumber/$columnNumber"
            if (elevation == 'S') {
                currElevationPoint.distance = 0
                startPoint = currElevationPoint
            }
        }
    }

    return startPoint
}

private fun getReachableNeighbors(
    input: List<String>, rowNumber: Int, columnNumber: Int,
    elevationMatrix: Array<Array<ElevationPoint>>
): Collection<ElevationPoint> {
    val elevation = getElevationValueAt(input, rowNumber, columnNumber)

    val neighbors = ArrayList<Pair<Char, ElevationPoint>>()
    // Top neighbor
    if (rowNumber > 0) {
        val elevationValue = getElevationValueAt(input, rowNumber - 1, columnNumber)
        neighbors.add(Pair(elevationValue, elevationMatrix[rowNumber - 1][columnNumber]))
    }
    // Right neighbor
    if (columnNumber < input[0].length - 1) {
        val elevationValue = getElevationValueAt(input, rowNumber, columnNumber + 1)
        neighbors.add(Pair(elevationValue, elevationMatrix[rowNumber][columnNumber + 1]))
    }
    // Bottom neighbor
    if (rowNumber < input.size - 1) {
        val elevationValue = getElevationValueAt(input, rowNumber + 1, columnNumber)
        neighbors.add(Pair(elevationValue, elevationMatrix[rowNumber + 1][columnNumber]))
    }
    // Left neighbor
    if (columnNumber > 0) {
        val elevationValue = getElevationValueAt(input, rowNumber, columnNumber - 1)
        neighbors.add(Pair(elevationValue, elevationMatrix[rowNumber][columnNumber - 1]))
    }

    return neighbors.filter { neighbor -> neighbor.first <= elevation + 1 }
        .map { neighbor -> neighbor.second }
}

private fun getElevationValueAt(input: List<String>, rowNumber: Int, columnNumber: Int): Char {
    val elevation = input[rowNumber][columnNumber]

    if (elevation == 'S')
        return 'a'
    if (elevation == 'E')
        return 'z'

    return elevation
}
