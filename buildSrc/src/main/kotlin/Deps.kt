@file:Suppress("ClassName", "ClassNaming", "Filename", "MatchingDeclarationName")

object deps {
    object dagger {
        const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    object junit {
        const val api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
        const val engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
    }

    object ktor {
        const val cio = "io.ktor:ktor-server-cio:${Versions.ktor}"
        const val gson = "io.ktor:ktor-gson:${Versions.ktor}"
    }

    object plugins {
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.0.0"
    }

    object sqldelight {
        const val driver = "com.squareup.sqldelight:jdbc-driver:${Versions.sqldelight}"
    }

    const val hikari = "com.zaxxer:HikariCP:4.0.2"
    const val logback = "ch.qos.logback:logback-classic:1.2.3"
    const val postgresJdbc = "org.postgresql:postgresql:${Versions.postgres}"
    const val jbcrypt = "org.mindrot:jbcrypt:${Versions.jbcrypt}"
}