package gradletemplate

import com.google.common.io.InputSupplier
import groovy.transform.Canonical

@Canonical
@SuppressWarnings("GroovyAssignabilityCheck")
class ScalaProject extends BaseProject {

    @Lazy private File mainScalaDir = {new File(mainSrcDir, 'scala')}()
    @Lazy private File testScalaDir = {new File(testSrcDir, 'scala')}()


    List<File> getSourceDirs() {
        List<File> srcDirs = super.getSourceDirs()
        srcDirs << mainScalaDir << testScalaDir
    }


    InputSupplier getBuildGradle() {
        Utils.getInputSupplierForResource('scala/build.gradle')
    }


    InputSupplier getGradleProperties() {
        Utils.getInputSupplierForResource('scala/gradle.properties')
    }

}
