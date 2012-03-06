package gradletemplate

import com.google.common.io.Files
import spock.lang.Subject

@Subject(GroovyProject)
@Mixin(MetaClassMixin)
class GroovyProjectSpec extends BaseProjectSpec {

    def createProject() {
        new GroovyProject()
    }


    def 'build groovy'() {
        given:
        project.parentDir = Files.createTempDir()
        project.name = 'GroovyProjectSpec_build'

        when:
        project.build()

        then:
        project.mainGroovyDir.exists()
        project.testGroovyDir.exists()

        cleanup:
        deleteDirectory(project.parentDir)
    }

}
