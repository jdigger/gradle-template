package gradletemplate

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

/**
 * The entry-point for the program.
 */
@Slf4j
class Main {

    static BaseProject createProject(CliOptions options) {
        BaseProject project

        if (options == null) {
            throw new BadOptionsException()
        }

        if (options.isGood) {
            project = options.projectClass.newInstance()
            if (options.testingMode)
                project.testMode = true
            project.name = options.projectName
            project.remoteRepo = options.remoteRepo
        }
        else {
            throw new BadOptionsException()
        }

        project
    }


    public static void main(String[] args) {
        CliOptions options = new CliOptions(args)
        try {
            BaseProject project = Main.createProject(options)
            project.build()
        }
        catch (BadOptionsException exp) {
            options.printUsage()
            System.exit(-1)
        }
        catch (Exception exp) {
            exp.printStackTrace()
            System.exit(-1)
        }
    }


    @InheritConstructors
    protected static class BadOptionsException extends RuntimeException {
    }

}
