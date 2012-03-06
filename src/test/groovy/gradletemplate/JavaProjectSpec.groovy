package gradletemplate

import com.google.common.io.Files
import spock.lang.Subject

@Subject(JavaProject)
@Mixin(MetaClassMixin)
class JavaProjectSpec extends BaseProjectSpec {

    def createProject() {
        new JavaProject()
    }


    def 'build java'() {
        given:
        project.parentDir = Files.createTempDir()
        project.name = 'JavaProjectSpec_build'

        when:
        project.build()

        then:
        project.mainJavaDir.exists()
        project.testJavaDir.exists()

        cleanup:
        Utils.deleteDirectory(project.parentDir)
    }

}
