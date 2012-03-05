package gradletemplate

import groovy.transform.Canonical

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
    }

}
