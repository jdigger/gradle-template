package gradletemplate

import git.Commit
import org.eclipse.jgit.api.Git
import spock.lang.Specification
import spock.lang.Subject

@Subject(BaseProject)
@Mixin(MetaClassMixin)
@SuppressWarnings(["GroovyAccessibility", "GroovyLabeledStatement"])
class BaseProjectSpec extends Specification {
    def project = createProject()

    def cleanup() {
        savedMetaClasses.each { clazz, metaClass ->
            GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
        }
    }


    def createProject() {
        new BaseProject()
    }


    def 'build base'() {
        given:
        project.testMode = true
        project.name = 'BaseProjectSpec_build'

        when:
        project.build()

        Git git = Git.open(project.projectDir)
        List<Commit> commits = git.log().call().collect {new Commit(git.repository, it)}.reverse()

        then:
        commits.size() == 4

        Commit commit0 = commits[0]
        commit0.message == 'bare project'
        commit0.tree.paths == ['.gitignore']

        Commit commit1 = commits[1]
        commit1.message == 'initial'
        commit1.tree.paths == ['.gitignore', 'README.md']

        Commit commit2 = commits[2]
        commit2.message == 'Adds the Gradle wrapper.'
        commit2.tree.paths.containsAll(['gradlew', 'gradlew.bat', '.gradle-wrapper'])

        project.srcDir.exists()
        project.mainSrcDir.exists()
        project.testSrcDir.exists()
        project.mainResourcesDir.exists()
        project.testResourcesDir.exists()

        cleanup:
        Utils.deleteDirectory(project.parentDir)
    }

}
