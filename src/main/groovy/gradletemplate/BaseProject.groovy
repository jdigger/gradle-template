package gradletemplate

import com.google.common.io.Files
import groovy.util.logging.Slf4j
import org.eclipse.jgit.api.Git

@Slf4j
@SuppressWarnings("GroovyAssignabilityCheck")
class BaseProject {
    String name

    protected boolean testMode = false

    @Lazy File parentDir = {testMode ? Files.createTempDir() : new File('.').canonicalFile}()
    @Lazy File projectDir = {new File(parentDir, name)}()

    @Lazy private File srcDir = {new File(projectDir, 'src')}()
    @Lazy private File mainSrcDir = {new File(srcDir, 'main')}()
    @Lazy private File testSrcDir = {new File(srcDir, 'test')}()
    @Lazy private File mainResourcesDir = {new File(mainSrcDir, 'resources')}()
    @Lazy private File testResourcesDir = {new File(testSrcDir, 'resources')}()


    List<File> getSourceDirs() {
        [srcDir, mainSrcDir, testSrcDir, mainResourcesDir, testResourcesDir]
    }


    void build() {
        projectDir.mkdir()

        createGitRepo()
        createMinimumFiles()
        createSrcDirStructure()
    }


    protected void createGitRepo() {
        Files.touch(new File(projectDir, '.gitignore'))
        Git.init().setDirectory(projectDir).call().with {
            add().addFilepattern('.gitignore').call()
            commit().setMessage('bare project').call()
        }
    }


    protected void createMinimumFiles() {
        Utils.streamToFile('common/gitignore', new File(projectDir, '.gitignore'))
        Utils.streamToFile('common/README.md', new File(projectDir, 'README.md'))
        Git.open(projectDir).with {
            add().addFilepattern('.gitignore').addFilepattern('README.md').call()
            commit().setMessage('initial').call()
        }
    }


    protected void createSrcDirStructure() {
        sourceDirs.each {File dir ->
            log.debug("Creating $dir")
            dir.mkdirs()
        }
    }

}
