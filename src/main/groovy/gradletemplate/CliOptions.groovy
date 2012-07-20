package gradletemplate

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class CliOptions {
    private static Logger log = LoggerFactory.getLogger(CliOptions)
    private CliBuilder cli

    String projectName
    Class<? extends BaseProject> projectClass
    boolean testingMode = false
    String remoteRepo


    CliOptions(String[] args) {
        cli = createCliBuilder()
        parse(args)
    }


    private static CliBuilder createCliBuilder() {
        CliBuilder cli = new CliBuilder(usage: 'gradle-template [options] <projectName>')
        cli.h(longOpt: 'help', 'Show this help message')
        cli.g(longOpt: 'groovy', 'Create a Groovy project (default)')
        cli.j(longOpt: 'java', 'Create a Java project')
        cli._(longOpt: 'X_testing', 'Create the project in a temporary directory')
        cli._(longOpt: 'remote', args: 1, argName: 'URL', 'URL to the remote repository')
        cli
    }


    protected void parse(String[] args) {
        OptionAccessor options = cli.parse(args)
        projectName = _getProjectName(options)
        projectClass = _getProjectClass(options)
        testingMode = _getTestingMode(options)
        remoteRepo = _getRemoteRepo(options)
    }


    @Override
    Object getProperty(String property) {
        if (property == 'isGood') {
            return getIsGood()
        }

        if (!isGood) throw new IllegalStateException("Not in a good state")
        this.metaClass.getMetaProperty(property).getProperty(this)
    }


    boolean getIsGood() {
        (projectName != null)
    }


    private static String _getProjectName(OptionAccessor options) {
        final List<String> arguments = options.arguments()
        final int argListLength = arguments.size()

        if (argListLength == 1) {
            arguments[0]
        }
        else if (argListLength == 0) {
            log.error("No project name")
            null
        }
        else {
            log.error("Too many project names")
            null
        }
    }


    private static Class<? extends BaseProject> _getProjectClass(OptionAccessor options) {
        if (options.j) {
            JavaProject
        }
        else {
            GroovyProject
        }
    }


    private static boolean _getTestingMode(OptionAccessor options) {
        options.X_testing
    }


    private static String _getRemoteRepo(OptionAccessor options) {
        !options.remote ? null : options.remote
    }


    void printUsage() {
        cli.usage()
    }

}
