package gradletemplate

/**
 * Misc functions
 */
class Utils {

    static void deleteDirectory(File dir) {
        if (dir == null) return
        dir.eachDir(this.&deleteDirectory)
        dir.eachFile { file ->
            file.delete()
        }
    }

}
