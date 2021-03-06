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
        Utils.getInputSupplierForResource('groovy/build.gradle')
    }


    InputSupplier getGradleProperties() {
        Utils.getInputSupplierForResource('groovy/gradle.properties')
    }

}
