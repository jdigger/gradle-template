# Description #

Project for creating new projects.


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


# Running #

To create a stand-alone application of this, run "`gradlew installApp`" or "`gradlew distZip`".

"installApp" will put the binaries in the `build/install/gradle-template` directory.

"distZip" will zip everything up and put it in the `build/distributions/` directory.

To see the list of options, run the program with no options supplied.


# ToDo #


# Done #

* Accept CLI arguments
* Create target top-level directory
* Initialize Git project
  * .gitignore
  * README.md
  * git add .gitignore README.md
* Create default .gitignore and README.md files
* Create src dir structure for kind of project (Java, Groovy)
* Gradle create "application" of this
* Create Gradle wrapper
* Create base build.gradle and gradle.properties files
* Add option to add GitHub/BitBucket support.
