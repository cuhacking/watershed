import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.squareup.sqldelight") version Versions.sqldelight
    application
}

dependencies {
    implementation(deps.ktor.cio)
    implementation(deps.dagger.dagger)
    kapt(deps.dagger.compiler)
    implementation(deps.sqldelight.driver)
    implementation(deps.hikari)
    implementation(deps.logback)

    testImplementation(kotlin("test-junit5"))
    testImplementation(deps.junit.api)
    testRuntimeOnly(deps.junit.engine)
    kaptTest(deps.dagger.compiler)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "ServerKt"
}

sqldelight {
    database("Database") {
        packageName = "com.cuhacking.watershed.db"
        dialect = "postgresql"
    }
}

kapt {
    correctErrorTypes = true
}