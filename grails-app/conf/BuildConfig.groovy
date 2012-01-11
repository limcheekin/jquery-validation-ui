grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// Disable SVN handling with release plugin
grails.release.scm.enabled = false
// Default repo to release is grailsCentral
grails.project.repos.default = "grailsCentral"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
		excludes 'xml-apis'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
		build("xom:xom:1.2.5") {
			export = false
		}
    }
	plugins {
		compile(":constraints:0.6.0")
		compile(":jquery:1.7.1")
		compile(":jquery-validation:1.7.3")
		compile(":resources:1.1.6") {
			excludes "webxml"
			export = false
		}
		compile(":webxml:1.4.1") {
			export = false
		}
		build(":new-doc:0.3.2") {
			export = false
			excludes "xom"
		}
		build(":release:1.0.0") {
			export = false
			excludes "xom"
		}
	}
}
