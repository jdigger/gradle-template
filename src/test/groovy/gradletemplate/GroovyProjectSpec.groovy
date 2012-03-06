package gradletemplate

import spock.lang.Subject

@Subject(GroovyProject)
@Mixin(MetaClassMixin)
class GroovyProjectSpec extends BaseProjectSpec {

    def createProject() {
        new GroovyProject()
    }


    def 'build groovy'() {
        given:
        project.testMode = true
        project.name = 'GroovyProjectSpec_build'

        when:
        project.build()

        then:
        project.mainGroovyDir.exists()
        project.testGroovyDir.exists()

        cleanup:
        Utils.deleteDirectory(project.parentDir)
    }

}
