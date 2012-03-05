package gradletemplate

import groovy.transform.Canonical
import org.eclipse.jgit.api.Git

@Canonical
class Project {
    String name
    private File _projectDir
    private File _parentDir


    void setParentDir(File dir) {
        this._parentDir = dir
    }


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
     * @return
     */
    File getProjectDir() {
        if (_projectDir == null) {
            _projectDir = new File(parentDir, name)
        }
        _projectDir
    }


    void build() {
        projectDir.mkdir()

        streamToFile('common/.gitignore', new File(projectDir, '.gitignore'))
        streamToFile('common/README.md', new File(projectDir, 'README.md'))
        Git.init().setDirectory(projectDir).call().with {
            add().addFilepattern('.gitignore').addFilepattern('README.md').call()
            commit().setMessage('initial').call()
        }
    }


    static void streamToFile(String inputResourceName, File outFile) {
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
