package gradletemplate

import groovy.transform.TupleConstructor
import java.lang.reflect.Method
import spock.lang.Specification

/**
 * A easy way to make sure that MetaClass changes that happen inside a Specification are cleaned up.
 *
 * Inspired by a combination of http://www.jworks.nl/2011/08/01/groovy-metaclass-magic-in-unit-tests/ and
 * the documentation for Grails' GrailsUnitTestCase.
 */
@Category(Specification)
class MetaClassMixin {
    Map<Class, MetaClass> savedMetaClasses = [:]

    static void myCleanup(Specification specification) {
        println "MetaClassMixin.cleanup"
        // Restore all the saved meta classes.
        specification.savedMetaClasses.each { clazz, metaClass ->
            GroovySystem.metaClassRegistry.setMetaClass(clazz, metaClass)
        }
    }

    /**
     * Use this method when you plan to perform some meta-programming
     * on a class. It ensures that any modifications you make will be
     * cleared at the end of the test.
     * @param clazz The class to register.
     */
    static void registerMetaClass(Specification specification, Class clazz) {
        //registerCleanup(specification)

        // If the class has already been registered, then there's
        // nothing to do.
        if (specification.savedMetaClasses.containsKey(clazz)) return

        // Save the class's current meta class.
        specification.savedMetaClasses[clazz] = clazz.metaClass

        // Create a new EMC for the class and attach it.
        def emc = new ExpandoMetaClass(clazz, true, true)
        emc.initialize()
        GroovySystem.metaClassRegistry.setMetaClass(clazz, emc)
    }

    // TODO: Don't know why this isn't working....
    protected static def registerCleanup(Specification specification) {
        Method cleanupMethod = findMethod(specification) {it.name =~ /$?cleanup$/}
        println "cleanupMethod  ${cleanupMethod}@${cleanupMethod.hashCode()}"
        if (cleanupMethod != null) {
            Method oldMethod = cleanupMethod
            specification.metaClass."${cleanupMethod.name}" = {->
                myCleanup(specification)
                oldMethod.invoke(specification)
            }
        }
        else {
            specification.metaClass.cleanup = {->
                myCleanup(specification)
            }
        }
        Method cleanupMethod2 = findMethod(specification) {it.name =~ /$?cleanup$/}
        println "cleanupMethod2 ${cleanupMethod2}@${cleanupMethod2.hashCode()}"
    }


    private static Method findMethod(Object instance, Closure predicate) {
        Method method = (Method) instance.class.methods.find(predicate)
        if (!method) {
            method = (Method) instance.metaClass.metaMethods.find(predicate)
        }
        method
    }


    def metaClassFor(Class clazz) {
        MetaClassMixin.registerMetaClass((Specification) this, clazz)

        new Delegate((ExpandoMetaClass) clazz.metaClass)
    }


    def metaClassFor(Object obj) {
        metaClassFor(obj.class)
    }


    def metaClassFor(Class clazz, Closure closure) {
        metaClassFor(clazz).call(closure)
    }

    /**
     * Simple proxy for
     */
    @TupleConstructor
    private static class Delegate {
        private ExpandoMetaClass target


        Delegate(ExpandoMetaClass target) {
            this.target = target
        }

        @Override
        void setProperty(String property, Object newValue) {
            target.setProperty(property, newValue)
        }


        def call(Closure closure) {
            target.define(closure)
        }

    }

}
