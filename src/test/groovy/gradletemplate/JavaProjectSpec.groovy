package gradletemplate

import spock.lang.Subject

@Subject(JavaProject)
@Mixin(MetaClassMixin)
class JavaProjectSpec extends BaseProjectSpec {

    def createProject() {
        new JavaProject()
    }


    def 'build java'() {
        given:
        project.testMode = true
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
