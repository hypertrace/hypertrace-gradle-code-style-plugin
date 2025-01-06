import org.hypertrace.gradle.publishing.License.APACHE_2_0

plugins {
  `java-gradle-plugin`
  id("org.hypertrace.repository-plugin") version "0.4.0"
  id("org.hypertrace.ci-utils-plugin") version "0.3.0"
  id("org.hypertrace.publish-plugin") version "1.0.4"
  id("org.owasp.dependencycheck") version "8.4.0"
}

group = "org.hypertrace.gradle.code.style"

java {
  targetCompatibility = JavaVersion.VERSION_11
  sourceCompatibility = JavaVersion.VERSION_11
}

dependencies {
  api("com.diffplug.spotless:spotless-plugin-gradle:6.25.0")
  constraints {
    implementation("com.squareup.okio:okio:3.4.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")
    implementation("org.eclipse.platform:org.eclipse.osgi:3.18.500")
  }
}

gradlePlugin {
  plugins {
    create("gradlePlugin") {
      id = "org.hypertrace.code-style-plugin"
      implementationClass = "org.hypertrace.gradle.code.style.CodeStylePlugin"
    }
  }
}

hypertracePublish {
  license.set(APACHE_2_0)
}

dependencyCheck {
  format = org.owasp.dependencycheck.reporting.ReportGenerator.Format.ALL.toString()
  suppressionFile = "owasp-suppressions.xml"
  scanConfigurations.add("runtimeClasspath")
  failBuildOnCVSS = 3.0F
}
