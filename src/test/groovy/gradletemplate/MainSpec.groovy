package gradletemplate

import gradletemplate.Main.Options
import spock.lang.Specification
import spock.lang.Subject

@Subject(Main)
@Mixin(MetaClassMixin)
class MainSpec extends Specification {
    Main main = new Main()

    def 'no args'() {
        when:
        main.run(new Options([] as String[]))

        then:
        thrown(Main.BadOptionsException)
    }


    def 'creates project top-level dir'() {
        given:
        Options options = new Options(['MainSpecTestProj'] as String[])

        expect:
        options.projectName == 'MainSpecTestProj'
    }

}
