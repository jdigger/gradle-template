package gradletemplate

import com.google.common.io.Files
import git.Commit
import org.eclipse.jgit.api.Git
import spock.lang.Specification
import spock.lang.Subject

@Subject(Project)
@Mixin(MetaClassMixin)
class ProjectSpec extends Specification {
    Project project = new Project()

    def 'build'() {
        given:
        project.parentDir = Files.createTempDir()
        project.name = 'ProjectSpec_build'

        when:
        project.build()

        and:
        new File(project.projectDir, '.git').isDirectory()

        Git git = Git.open(project.projectDir)
        List<Commit> commits = git.log().call().collect {new Commit(git.repository, it)}
        commits.size() == 1
        Commit commit = commits[0]

        then:
        commit.message == 'initial'
        commit.tree.paths == ['.gitignore', 'README.md']

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
