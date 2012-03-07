package gradletemplate

import spock.lang.Specification
import spock.lang.Subject

@SuppressWarnings("GroovyLabeledStatement")
@Subject(Main)
@Mixin(MetaClassMixin)
class MainSpec extends Specification {
    BaseProject project


    def cleanup() {
        Utils.deleteDirectory(project?.parentDir)
    }


    def 'no args'() {
        when:
        Main.createProject(new CliOptions([] as String[]))

        then:
        thrown(Main.BadOptionsException)
    }


    def 'creates basic default project'() {
        given:
        CliOptions options = new CliOptions(['--X_testing', 'MainSpecTestProj'] as String[])

        when:
        project = Main.createProject(options)

        then:
        project.name == 'MainSpecTestProj'
        project.class == GroovyProject
        options.testingMode
        !options.remoteRepo

        when:
        project.build()

        then:
        project.mainGroovyDir.exists()
    }


    def 'creates basic Java project'() {
        given:
        CliOptions options = new CliOptions(['--X_testing', '-j', 'MainSpecTestProj'] as String[])

        when:
        project = Main.createProject(options)

        then:
        project.name == 'MainSpecTestProj'
        project.class == JavaProject
        options.testingMode
        !options.remoteRepo

        when:
        project.build()

        then:
        project.mainJavaDir.exists()
    }


    def 'creates basic remote Groovy project'() {
        given:
        CliOptions options = new CliOptions(['--X_testing', '-g', '--remote', 'git@github.com:jdigger/gradle-template.git', 'MainSpecTestProj'] as String[])

        when:
        project = Main.createProject(options)

        then:
        project.name == 'MainSpecTestProj'
        project.class == GroovyProject
        options.testingMode
        options.remoteRepo == 'git@github.com:jdigger/gradle-template.git'

        when:
        project.build()

        then:
        project.mainGroovyDir.exists()
    }

}
