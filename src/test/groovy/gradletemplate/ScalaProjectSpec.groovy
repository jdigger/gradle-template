package gradletemplate

import spock.lang.Subject

@Subject(JavaProject)
@Mixin(MetaClassMixin)
class ScalaProjectSpec extends BaseProjectSpec {

    def createProject() {
        new ScalaProject()
    }


    def 'build scala'() {
        given:
        project.testMode = true
        project.name = 'ScalaProjectSpec_build'

        when:
        project.build()

        then:
        project.mainScalaDir.exists()
        project.testScalaDir.exists()

        cleanup:
        Utils.deleteDirectory(project.parentDir)
    }

}
