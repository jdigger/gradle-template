package gradletemplate

import com.google.common.io.Files
import com.google.common.io.InputSupplier
import groovy.util.logging.Slf4j
import org.eclipse.jgit.api.Git

@Slf4j
@SuppressWarnings("GroovyAssignabilityCheck")
class BaseProject {
    String name

    protected boolean testMode = false

    @Lazy File parentDir = {testMode ? Files.createTempDir() : new File('.').canonicalFile}()
    @Lazy File projectDir = {new File(parentDir, name)}()
    @Lazy File gradleWrapperBootstrapDir = {new File(projectDir, '.gradle-wrapper')}()

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
        createGradleWrapper()
        createGradleBuildFiles()
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


    protected def createGradleWrapper() {
        GradleWrapper.copy(this)

        Git.open(projectDir).with {
            add().addFilepattern('gradlew').addFilepattern('gradlew.bat').addFilepattern('.gradle-wrapper').call()
            commit().setMessage('Adds the Gradle wrapper.').call()
        }
    }


    protected void createGradleBuildFiles() {
        Files.copy(buildGradle, new File(projectDir, 'build.gradle'))
        Files.copy(gradleProperties, new File(projectDir, 'gradle.properties'))
        Git.open(projectDir).with {
            add().addFilepattern('build.gradle').addFilepattern('gradle.properties').call()
            commit().setMessage('Adds a basic Gradle build configuration.').call()
        }
    }


    InputSupplier getBuildGradle() {
        Utils.getInputSupplierForResource('groovy/build.gradle')
    }


    InputSupplier getGradleProperties() {
        Utils.getInputSupplierForResource('groovy/gradle.properties')
    }

}
