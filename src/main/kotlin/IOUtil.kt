class IOUtil {
    companion object {
        fun readResourceFile(path: String): List<String>? {
            return {}.javaClass.getResource(path)?.readText()?.split("\n")
        }
    }
}
