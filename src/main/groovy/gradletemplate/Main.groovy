package gradletemplate

import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

/**
 * The entry-point for the program.
 */
@Slf4j
class Main {

    void run(Options options) {
        if (options == null) {
            throw new BadOptionsException()
        }

        if (options.isGood) {
            BaseProject project = new BaseProject(name: options.projectName)
            project.build()
        }
        else {
            throw new BadOptionsException()
        }
    }


    public static void main(String[] args) {
        Options options = new Options(args)
        try {
            Main main = new Main()
            main.run(options)
        }
        catch (BadOptionsException exp) {
            options.cli.usage()
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


    static class Options {

        CliBuilder cli
        boolean isGood = false
        private String _projectName
        private Class<? extends BaseProject> projectClass


        protected Options() {
            cli = new CliBuilder(usage: 'gradletemplate [options] <projectName>')
            cli.h(longOpt: 'help', 'Show this help message')
            cli.g(longOpt: 'groovy', 'Create a Groovy project (default)')
            cli.j(longOpt: 'java', 'Create a Java project')
        }


        Options(String[] args) {
            this()
            parse(args)
        }


        protected def parse(String[] args) {
            if (args.length == 0) {
                isGood = false
            }
            else {
                def options = cli.parse(args)

                if (options.arguments().size() == 1) {
                    _projectName = options.arguments()[0]
                    if (options.j) {

                    }
                    isGood = true
                }
                else {
                    log.error("No project name")
                    isGood = false
                }
            }
        }


        String getProjectName() {
            verifyGood()
            _projectName
        }


        private void verifyGood() {
            if (!isGood) throw new IllegalStateException("Not in a good state")
        }

    }

}
