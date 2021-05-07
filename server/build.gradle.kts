import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.4.32"
    id("com.squareup.sqldelight") version "1.4.3"
    id("org.flywaydb.flyway") version "7.8.2"
    application
}

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.serialization)

    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)

    implementation(libs.sqldelight.driver.jdbc)
    implementation(libs.hikari)
    implementation(libs.logback)
    implementation(libs.jdbc.postgres)
    implementation(libs.kaml)
    implementation(libs.jbcrypt)

    testImplementation(kotlin("test-junit5"))
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