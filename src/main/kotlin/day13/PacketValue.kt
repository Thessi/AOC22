package day13

import kotlin.math.max

data class PacketValue(
    var parent: PacketValue?,
    var value: Int? = null,
    var list: ArrayList<PacketValue> = ArrayList(),
    var isMarker: Boolean = false
) : Comparable<PacketValue> {
    override fun compareTo(other: PacketValue): Int {
        if (value != null && other.value != null) {
            return value!!.compareTo(other.value!!)
        }

        var packetList1: ArrayList<PacketValue> = list
        var packetList2: ArrayList<PacketValue> = other.list
        if (value != null) {
            packetList1 = arrayListOf(this)
        }
        if (other.value != null) {
            packetList2 = arrayListOf(other)
        }

        for (i in 0 until max(packetList1.size, packetList2.size)) {
            if (i >= packetList1.size) {
                return -1
            }
            if (i >= packetList2.size) {
                return 1
            }
            val compare = packetList1[i].compareTo(packetList2[i])
            if (compare != 0) {
                return compare
            }
        }

        return 0
    }
}
