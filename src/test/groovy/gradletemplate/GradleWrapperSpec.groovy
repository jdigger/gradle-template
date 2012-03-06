package gradletemplate

import spock.lang.Specification
import spock.lang.Subject

@Subject(GradleWrapper)
class GradleWrapperSpec extends Specification {

    def 'copy wrapper files'() {
        given:
        BaseProject project = new BaseProject()
        project.testMode = true
        project.name = 'GradleWrapperSpec_build'

        when:
        project.build()
        GradleWrapper.copy(project)

        then:
        new File(project.projectDir, 'gradlew').exists()
        new File(project.projectDir, 'gradlew').canExecute()
        new File(project.projectDir, 'gradlew.bat').exists()
        new File(project.gradleWrapperBootstrapDir, 'gradle-wrapper.jar').exists()
        new File(project.gradleWrapperBootstrapDir, 'gradle-wrapper.properties').exists()

        cleanup:
        Utils.deleteDirectory(project.parentDir)
    }

}
