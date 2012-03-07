package gradletemplate

import com.google.common.io.InputSupplier
import groovy.transform.Canonical

@Canonical
@SuppressWarnings("GroovyAssignabilityCheck")
class JavaProject extends BaseProject {

    @Lazy private File mainJavaDir = {new File(mainSrcDir, 'java')}()
    @Lazy private File testJavaDir = {new File(testSrcDir, 'java')}()


    List<File> getSourceDirs() {
        List<File> srcDirs = super.getSourceDirs()
        srcDirs << mainJavaDir << testJavaDir
    }


    InputSupplier getBuildGradle() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream('java/build.gradle')
        ({inputStream} as InputSupplier<InputStream>)
    }


    InputSupplier getGradleProperties() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream('java/gradle.properties')
        ({inputStream} as InputSupplier<InputStream>)
    }

}
