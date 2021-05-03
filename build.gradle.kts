buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
    }
}

group = "com.cuhacking.watershed"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}
