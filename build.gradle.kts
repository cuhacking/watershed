buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(deps.plugins.kotlin)
        classpath(deps.plugins.ktlint)
    }
}

group = "com.cuhacking.watershed"
version = "1.0-SNAPSHOT"

allprojects {
    //apply(plugin = "org.jlleitschuh.gradle.ktlint")
    repositories {
        mavenCentral()
    }

    /*configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        filter {
            exclude("**\\generated\\**")
        }
    }*/
}
