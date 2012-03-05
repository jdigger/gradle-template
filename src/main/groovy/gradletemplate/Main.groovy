package gradletemplate

import groovy.transform.InheritConstructors

/**
 * The entry-point for the program.
 */
class Main {

    Main() {

    }


    void run(Options options) {
        if (options == null) {
            throw new BadOptionsException()
        }

        if (options.isGood) {

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


        protected Options() {
            cli = new CliBuilder(usage: 'gradletemplate')
            cli.h(longOpt: 'help', 'Show this help message')
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
                isGood = true

            }
        }


    }

}
