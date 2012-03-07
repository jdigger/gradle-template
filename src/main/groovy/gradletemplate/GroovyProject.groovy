package gradletemplate

import com.google.common.io.InputSupplier
import groovy.transform.Canonical

@Canonical
@SuppressWarnings("GroovyAssignabilityCheck")
class GroovyProject extends BaseProject {

    @Lazy private File mainGroovyDir = {new File(mainSrcDir, 'groovy')}()
    @Lazy private File testGroovyDir = {new File(testSrcDir, 'groovy')}()


    List<File> getSourceDirs() {
        List<File> srcDirs = super.getSourceDirs()
        srcDirs << mainGroovyDir << testGroovyDir
    }


    InputSupplier getBuildGradle() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream('groovy/build.gradle')
        assert inputStream
        ({inputStream} as InputSupplier<InputStream>)
    }


    InputSupplier getGradleProperties() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream('groovy/gradle.properties')
        assert inputStream
        ({inputStream} as InputSupplier<InputStream>)
    }

}
