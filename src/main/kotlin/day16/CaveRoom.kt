package day16

data class CaveRoom(var valveLabel: String = "", var flowRate: Int = 0, var connectedRooms: List<CaveRoom> = ArrayList())
