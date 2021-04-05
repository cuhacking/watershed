import Versions.flyway
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.squareup.sqldelight") version Versions.sqldelight
    id("org.flywaydb.flyway") version Versions.flyway
    application
}

dependencies {
    implementation(deps.ktor.cio)
    implementation(deps.ktor.gson)
    implementation(deps.dagger.dagger)
    kapt(deps.dagger.compiler)
    implementation(deps.sqldelight.driver)
    implementation(deps.hikari)
    implementation(deps.logback)
    implementation(deps.postgresJdbc)
    implementation(deps.jbcrypt)

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
        sourceFolders = listOf("sqldelight")
        deriveSchemaFromMigrations = true
        migrationOutputDirectory = file("$buildDir/resources/main/migrations")
        migrationOutputFileFormat = ".sql"
    }
}

kapt {
    correctErrorTypes = true
}

flyway {
    url = "jdbc:postgresql://localhost:5432/watershed"
    user = "postgres"
    password = "password"
    locations = arrayOf("filesystem:$buildDir/resources/main/migrations")
}
