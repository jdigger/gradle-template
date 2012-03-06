package gradletemplate

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

}
