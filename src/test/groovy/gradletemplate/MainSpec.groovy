package gradletemplate

import gradletemplate.Main.Options
import spock.lang.Specification
import spock.lang.Subject

@SuppressWarnings("GroovyLabeledStatement")
@Subject(Main)
@Mixin(MetaClassMixin)
class MainSpec extends Specification {

    def 'no args'() {
        when:
        Main.createProject(new Options([] as String[]))

        then:
        thrown(Main.BadOptionsException)
    }


    def 'creates basic default project'() {
        given:
        Options options = new Options(['--X_testing', 'MainSpecTestProj'] as String[])

        when:
        BaseProject project = Main.createProject(options)

        then:
        project.name == 'MainSpecTestProj'
        project.class == GroovyProject
        options.testingMode

        when:
        project.build()

        then:
        project.mainGroovyDir.exists()

        cleanup:
        Utils.deleteDirectory(project?.parentDir)
    }


    def 'creates basic Java project'() {
        given:
        Options options = new Options(['--X_testing', '-j', 'MainSpecTestProj'] as String[])

        when:
        BaseProject project = Main.createProject(options)

        then:
        project.name == 'MainSpecTestProj'
        project.class == JavaProject
        options.testingMode

        when:
        project.build()

        then:
        project.mainJavaDir.exists()

        cleanup:
        Utils.deleteDirectory(project?.parentDir)
    }

}
