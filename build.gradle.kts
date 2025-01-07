import org.hypertrace.gradle.publishing.License.APACHE_2_0

plugins {
  `java-gradle-plugin`
  id("org.hypertrace.repository-plugin") version "0.4.0"
  id("org.hypertrace.ci-utils-plugin") version "0.3.0"
  id("org.hypertrace.publish-plugin") version "1.0.4"
  id("org.owasp.dependencycheck") version "8.4.0"
  id("com.diffplug.spotless") version "7.0.0"
}

group = "org.hypertrace.gradle.code.style"

java {
  targetCompatibility = JavaVersion.VERSION_11
  sourceCompatibility = JavaVersion.VERSION_11
}

dependencies {
  api("com.diffplug.spotless:spotless-plugin-gradle:7.0.0")
  implementation("build.buf:buf-gradle-plugin:0.10.0")
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

spotless {
  java {
    importOrder()
    removeUnusedImports()
    googleJavaFormat("1.17.0")
  }
  kotlin {
    ktlint()
  }
}
