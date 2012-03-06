package gradletemplate

/**
 * Misc functions
 */
class Utils {

    static void deleteDirectory(File dir) {
        if (dir == null) return
        if (dir.canonicalFile == new File('.').canonicalFile) {
            throw new IllegalArgumentException("Can't delete current directory")
        }
        dir.eachDir(this.&deleteDirectory)
        dir.eachFile { file ->
            file.delete()
        }
    }


    static void streamToFile(String inputResourceName, File outFile) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(inputResourceName)
        if (inputStream == null) {
            throw new IllegalArgumentException("Could not find ${inputResourceName} on the classpath")
        }
        outFile.withPrintWriter {PrintWriter writer ->
            inputStream.withReader {Reader reader ->
                reader.eachLine {
                    writer.println(it)
                }
            }
        }
    }

}
