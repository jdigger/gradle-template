package gradletemplate

import com.google.common.io.Files
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

        then:
        new File(project.parentDir, project.name).exists()
        new File(project.parentDir, project.name).isDirectory()

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
