package day7

data class AocDirectory(
    var dirName: String,
    var parent: AocDirectory? = null,
    var directories: MutableList<AocDirectory> = ArrayList(),
    var files: MutableList<AocFile> = ArrayList()
) {
    fun getTotalSize(): Long {
        return directories.sumOf { it.getTotalSize() } + files.sumOf { it.fileSize }
    }

    fun getSubdirectory(dirName: String): AocDirectory {
        val foundDir = directories.find { it.dirName == dirName }
        if (foundDir != null) {
            return foundDir
        }

        val newDir = AocDirectory(dirName, this)
        directories.add(newDir)

        return newDir
    }

    fun addFile(fileName: String, fileSize: Long) {
        val foundFile = files.find { it.fileName == fileName }

        if (foundFile != null) {
            foundFile.fileSize = fileSize
            return
        }

        files.add(AocFile(this, fileName, fileSize))
    }
}
