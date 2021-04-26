import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.squareup.sqldelight") version Versions.sqldelight
    id("com.google.devtools.ksp") apply true
    application
}

kotlin {
    // Add generated kotlin-inject sources
    sourceSets {
        getByName("main") {
            kotlin.srcDir("$buildDir/generated/ksp/main/kotlin")
        }
    }

    dependencies {
        implementation(deps.ktor.cio)
        implementation(deps.sqldelight.driver)
        implementation(deps.hikari)
        implementation(deps.logback)
        implementation(deps.postgresJdbc)

        ksp(deps.inject.compiler)
        implementation(deps.inject.runtime)

        testImplementation(kotlin("test-junit5"))
        testImplementation(deps.junit.api)
        testRuntimeOnly(deps.junit.engine)
    }
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
