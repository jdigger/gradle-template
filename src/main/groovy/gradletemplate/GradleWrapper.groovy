package gradletemplate

import com.google.common.io.Files
import groovy.util.logging.Slf4j

@Slf4j
class GradleWrapper {

    static def copy(BaseProject project) {
        List<File> scriptFiles = findScripts()
        List<File> bootstrapFiles = findBootstrapFiles()

        copyFiles(scriptFiles, project.projectDir)

        setExecutableBit(project)

        File wrapperDir = project.gradleWrapperBootstrapDir
        if (!wrapperDir.exists()) {
            wrapperDir.mkdirs()
        }

        copyFiles(bootstrapFiles, wrapperDir)
    }


    private static def setExecutableBit(BaseProject project) {
        File gradlewSh = new File(project.projectDir, 'gradlew')
        log.debug "Setting executable bit for $gradlewSh"
        gradlewSh.setExecutable(true)
    }


    private static copyFiles(List<File> files, File toDir) {
        files.each {File fromFile ->
            File toFile = new File(toDir, fromFile.name)
            log.debug "Copying ${fromFile} -> ${toFile}"
            Files.copy(fromFile, toFile)
        }
    }


    protected static List<File> findScripts() {
        File curDir = new File('.')
        if (new File(curDir, 'gradlew').exists()) {
            [new File(curDir, 'gradlew'), new File(curDir, 'gradlew.bat')]
        }
        else {
            File appDir = findAppDir()
            def wrapperDir = new File(appDir, 'gradle-wrapper')
            if (wrapperDir.exists()) {
                [new File(wrapperDir, 'gradlew'), new File(wrapperDir, 'gradlew.bat')]
            }
            else {
                throw new IllegalStateException("Can't find wrapper files")
            }
        }
    }


    protected static List<File> findBootstrapFiles() {
        File curDir = new File('.')
        File wrapperDir = new File(curDir, '.gradle-wrapper')
        if (wrapperDir.exists()) {
            [new File(wrapperDir, 'gradle-wrapper.jar'), new File(wrapperDir, 'gradle-wrapper.properties')]
        }
        else {
            File appDir = findAppDir()
            wrapperDir = new File(appDir, 'gradle-wrapper/.gradle-wrapper')
            if (wrapperDir.exists()) {
                [new File(wrapperDir, 'gradle-wrapper.jar'), new File(wrapperDir, 'gradle-wrapper.properties')]
            }
            else {
                throw new IllegalStateException("Can't find wrapper files")
            }
        }
    }


    protected static File findAppDir() {
        String resourceName = GradleWrapper.simpleName + '.class'
        log.debug("trying to load $resourceName")
        URL resource = GradleWrapper.class.getResource(resourceName)
        // toURI().toURL() prevents a bug in toURL() when spaces are in the filename
        String fileName = resource.toURI().toURL().file
        String jarFilename = (fileName =~ /file:(.*)!/)[0][1]
        File appDir = new File(jarFilename).parentFile.parentFile
        log.debug "appDir: $appDir from filename: $fileName and jarFilename: $jarFilename"
        appDir
    }

}
