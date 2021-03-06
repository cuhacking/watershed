import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.*
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.5.20"
    id("com.squareup.sqldelight") version "1.5.0"
    id("org.flywaydb.flyway") version "7.8.2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.auth.core)
    implementation(libs.ktor.auth.jwt)

    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)

    implementation(libs.sqldelight.driver.jdbc)
    implementation(libs.hikari)
    implementation(libs.logback)
    implementation(libs.jdbc.postgres)
    implementation(libs.kaml)
    implementation(libs.jbcrypt)
    implementation(libs.kotlinArgparser)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testImplementation(libs.ktor.server.test)
    testImplementation(libs.bundles.testContainers)
    testImplementation(libs.flyway.core)
    testRuntimeOnly(libs.junit.engine)
    kaptTest(libs.dagger.compiler)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events(FAILED, STANDARD_ERROR, SKIPPED)
        exceptionFormat = FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register("migrate") {
    val generateMigrations = tasks["generateMainDatabaseMigrations"]
    val runMigrations = tasks["flywayMigrate"]
    dependsOn(generateMigrations)
    dependsOn(runMigrations)
    runMigrations.mustRunAfter(generateMigrations)
}

application {
    mainClassName = "com.cuhacking.watershed.ServerKt"
}

sqldelight {
    database("Database") {
        packageName = "com.cuhacking.watershed.db"
        dialect = "postgresql"
        sourceFolders = listOf("sqldelight")
        deriveSchemaFromMigrations = true
        migrationOutputDirectory = file("$buildDir/resources/main/migrations")
        migrationOutputFileFormat = ".sql"
    }
}

flyway {
    url = "jdbc:postgresql://localhost:5432/watershed"
    user = "postgres"
    password = "password"
    locations = arrayOf("filesystem:$buildDir/resources/main/migrations")
}

kapt {
    correctErrorTypes = true
}