import org.hypertrace.gradle.publishing.License.APACHE_2_0

plugins {
  `java-gradle-plugin`
  id("org.hypertrace.ci-utils-plugin") version "0.1.1"
  id("org.hypertrace.publish-plugin") version "0.3.1"
}

group = "org.hypertrace.gradle.code.style"

java {
  targetCompatibility = JavaVersion.VERSION_1_8
  sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
  mavenCentral()
}

dependencies {
  api("com.diffplug.spotless:spotless-plugin-gradle:5.11.0")
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