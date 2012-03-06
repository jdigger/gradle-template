package gradletemplate

import com.google.common.io.Files
import com.google.common.io.InputSupplier

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
        Files.copy({inputStream} as InputSupplier<InputStream>, outFile)
    }

}
