buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(deps.plugins.kotlin)
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.4.32-1.0.0-alpha08" apply false
}

group = "com.cuhacking.watershed"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
