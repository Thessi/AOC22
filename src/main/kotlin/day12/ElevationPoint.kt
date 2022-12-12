package day12

data class ElevationPoint(
    val neighbors: ArrayList<ElevationPoint> = ArrayList(),
    var isDestination: Boolean = false,
    var location: String = "-1/-1",
    var distance: Int = Int.MAX_VALUE,
    var visited: Boolean = false
)
