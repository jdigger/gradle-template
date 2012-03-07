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
        Utils.getInputSupplierForResource('java/build.gradle')
    }


    InputSupplier getGradleProperties() {
        Utils.getInputSupplierForResource('java/gradle.properties')
    }

}
