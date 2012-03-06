package gradletemplate

import com.google.common.io.Files
import git.Commit
import org.eclipse.jgit.api.Git
import spock.lang.Specification
import spock.lang.Subject

@Subject(BaseProject)
@Mixin(MetaClassMixin)
@SuppressWarnings("GroovyAccessibility")
class BaseProjectSpec extends Specification {
    def project = createProject()


    def createProject() {
        new BaseProject()
    }


    def 'build base'() {
        given:
        project.parentDir = Files.createTempDir()
        project.name = 'BaseProjectSpec_build'

        when:
        project.build()

        and:
        new File(project.projectDir, '.git').isDirectory()

        Git git = Git.open(project.projectDir)
        List<Commit> commits = git.log().call().collect {new Commit(git.repository, it)}.reverse()

        then:
        commits.size() == 2

        Commit commit0 = commits[0]
        commit0.message == 'bare project'
        commit0.tree.paths == ['.gitignore']

        Commit commit1 = commits[1]
        commit1.message == 'initial'
        commit1.tree.paths == ['.gitignore', 'README.md']

        project.srcDir.exists()
        project.mainSrcDir.exists()
        project.testSrcDir.exists()
        project.mainResourcesDir.exists()
        project.testResourcesDir.exists()

        cleanup:
        deleteDirectory(project.parentDir)
    }


    void deleteDirectory(File dir) {
        dir.eachDir(this.&deleteDirectory)
        dir.eachFile { file ->
            file.delete()
        }
    }

}
