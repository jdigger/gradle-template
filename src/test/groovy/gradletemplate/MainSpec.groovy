package gradletemplate

import groovy.util.logging.Slf4j
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PushCommand
import org.eclipse.jgit.transport.PushResult
import spock.lang.Specification
import spock.lang.Subject

@Subject(Main)
@Mixin(MetaClassMixin)
@SuppressWarnings("GroovyLabeledStatement")
class MainSpec extends Specification {
    BaseProject project


    def cleanup() {
        savedMetaClasses.each { clazz, metaClass ->
            GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
        }

        Utils.deleteDirectory(project?.parentDir)
    }


    def 'no args'() {
        when:
        Main.createProject(new CliOptions([] as String[]))

        then:
        thrown(Main.BadOptionsException)
    }


    def 'creates basic groovy project'() {
        given:
        CliOptions options = new CliOptions(['--X_testing', '-g', 'MainSpecTestProj'] as String[])

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


    def 'creates basic Scala project'() {
        given:
        CliOptions options = new CliOptions(['--X_testing', '-s', 'MainSpecTestProj'] as String[])

        when:
        project = Main.createProject(options)

        then:
        project.name == 'MainSpecTestProj'
        project.class == ScalaProject
        options.testingMode
        !options.remoteRepo

        when:
        project.build()

        then:
        project.mainScalaDir.exists()
    }


    def 'creates basic remote Groovy project'() {
        given:
        CliOptions options = new CliOptions(['--X_testing', '-g', '--remote', 'git@github.com:daviesd/testtesttest.git', 'MainSpecTestProj'] as String[])

        when:
        project = Main.createProject(options)

        then:
        project.name == 'MainSpecTestProj'
        project.class == GroovyProject
        options.testingMode
        options.remoteRepo == 'git@github.com:daviesd/testtesttest.git'

        when:
        use(NoopPushCommand) {
            project.build()
        }

        then:
        project.mainGroovyDir.exists()

        when:
        Git.open(project.projectDir).repository.config.with {
            assert getString('remote', 'origin', 'url') == options.remoteRepo
            assert getString('remote', 'origin', 'fetch') == '+refs/heads/*:refs/remotes/origin/*'
            assert getString('branch', 'master', 'remote') == 'origin'
            assert getString('branch', 'master', 'merge') == 'refs/heads/master'
        }

        then:
        1
    }


    @Slf4j
    static class NoopPushCommand {
        static Iterable<PushResult> call(PushCommand pushCommand) {
            log.debug "PushCommand called"
            null
        }
    }

}
