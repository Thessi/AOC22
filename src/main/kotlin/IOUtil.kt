class IOUtil {
    companion object {
        fun readResourceFile(path: String): List<String>? {
            val lines = {}.javaClass.getResource(path)?.readText()?.split("\r\n")
            if (lines?.get(lines.size -1) == "") {
                return lines.subList(0, lines.size - 1).toList()
            }
            return null
        }
    }
}
