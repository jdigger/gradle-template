package gradletemplate

import com.google.common.io.Files
import groovy.util.logging.Slf4j
import org.eclipse.jgit.api.Git

//@Canonical
@Slf4j
@SuppressWarnings("GroovyAssignabilityCheck")
class BaseProject {
    String name
    private File _projectDir
    private File _parentDir

    @Lazy private File srcDir = {new File(projectDir, 'src')}()
    @Lazy private File mainSrcDir = {new File(srcDir, 'main')}()
    @Lazy private File testSrcDir = {new File(srcDir, 'test')}()
    @Lazy private File mainResourcesDir = {new File(mainSrcDir, 'resources')}()
    @Lazy private File testResourcesDir = {new File(testSrcDir, 'resources')}()


    void setParentDir(File dir) {
        this._parentDir = dir
    }

    /**
     * Defaults to the current directory
     * @return never null
     */
    File getParentDir() {
        if (_parentDir == null) {
            File curDir = new File('.')
            _parentDir = curDir.canonicalFile
        }
        _parentDir
    }


    void setProjectDir(File dir) {
        this._projectDir = dir
    }

    /**
     * Defaults to {@link #getParentDir()}/{@link #name}
     * @return never null
     */
    File getProjectDir() {
        if (_projectDir == null) {
            _projectDir = new File(parentDir, name)
        }
        _projectDir
    }


    List<File> getSourceDirs() {
        [srcDir, mainSrcDir, testSrcDir, mainResourcesDir, testResourcesDir]
    }


    void build() {
        projectDir.mkdir()

        createGitRepo()
        createMinimumFiles()
        createSrcDirStructure()
    }


    protected void createSrcDirStructure() {
        sourceDirs.each {File dir ->
            log.debug("Creating $dir")
            dir.mkdirs()
        }
    }


    protected void createMinimumFiles() {
        streamToFile('common/.gitignore', new File(projectDir, '.gitignore'))
        streamToFile('common/README.md', new File(projectDir, 'README.md'))
        Git.open(projectDir).with {
            add().addFilepattern('.gitignore').addFilepattern('README.md').call()
            commit().setMessage('initial').call()
        }
    }


    protected void createGitRepo() {
        Files.touch(new File(projectDir, '.gitignore'))
        Git.init().setDirectory(projectDir).call().with {
            add().addFilepattern('.gitignore').call()
            commit().setMessage('bare project').call()
        }
    }


    protected static void streamToFile(String inputResourceName, File outFile) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(inputResourceName)
        outFile.withPrintWriter {PrintWriter writer ->
            inputStream.withReader {Reader reader ->
                reader.eachLine {
                    writer.println(it)
                }
            }
        }
    }

}
