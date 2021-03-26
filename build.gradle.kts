buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(deps.plugins.kotlin)
    }
}

group = "com.cuhacking.watershed"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}
