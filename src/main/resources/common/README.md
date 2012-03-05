# Description #

This project...

# Setup #

* ...
* ...

# Building #

The project is built using [Gradle](http://gradle.org), which provides bootstrapping wrapper support so that
the only thing that needs to be installed on the build machine is Java (because of Java licensing distribution rules).

You can run "`gradlew`" (i.e., "`./gradlew`" on *nix or "`gradlew.bat`" on Windows) from the top level directory,
followed by the desired tasks.

For example, to do a clean build -- including running all quality tests and creating the distribution jars --
simply run "`gradlew clean build`"

To see a complete list of available tasks, run "`gradlew tasks`".

To create project files for IntelliJ IDEA or STS/Eclipse, specify that as the Gradle task.
(i.e., "`gradlew idea`" or "`gradlew eclipse`")
