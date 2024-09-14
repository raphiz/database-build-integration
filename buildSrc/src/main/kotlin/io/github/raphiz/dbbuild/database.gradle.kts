import io.github.raphiz.dbbuild.FlywayMigrateTask

plugins {
    kotlin
    id("org.jooq.jooq-codegen-gradle")
}


val dbJdbcUrl = "jdbc:postgresql://localhost:5432/"
val dbUsername = "localdev"
val dbPassword = "*****"
val dbTemplateDbName = "template1"
val dbJooqOutputDirectory = layout.buildDirectory.dir("generated/jooq").get().also { it.asFile }
val dbMigrationsLocation = layout.projectDirectory.dir("src/main/resources/db/migration")
val dbTemplateDbUrl = "${dbJdbcUrl}${dbTemplateDbName}"

// Use custom FlywayMigrate task because the official flyway gradle plugin is badly maintained and not compatible with the Gradle Configuration Cache
// see https://github.com/flyway/flyway/issues/3550
val flywayMigrate by tasks.registering(FlywayMigrateTask::class) {
    url.convention(dbTemplateDbUrl)
    username.convention(dbUsername)
    password.convention(dbPassword)
    migrationsLocation = dbMigrationsLocation
}

sourceSets {
    main {
        java.srcDirs(dbJooqOutputDirectory)
    }
}
tasks.compileKotlin.configure {
    dependsOn(tasks.jooqCodegen)
}

dependencies {
    jooqCodegen("org.postgresql:postgresql:42.7.4")
}

tasks.test {
    dependsOn(flywayMigrate)
    environment("DB_URL", dbJdbcUrl)
    environment("DB_USER", dbUsername)
    environment("DB_PASSWORD", dbPassword)
    environment("DB_TEMPLATE_NAME", "template1")
    systemProperty("org.jooq.no-logo", "true")
    systemProperty("org.jooq.no-tips", "true")
}

tasks.jooqCodegen.configure {
    // db must be migrated before jooq can generate the code.
    // The jOOQ code mostly depends on the flyway locations and can be cached when the migrations have not changed
    dependsOn(flywayMigrate)
    inputs.files(dbMigrationsLocation)
}

jooq {
    configuration {
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            jdbc {
                driver = "org.postgresql.Driver"
                url = dbTemplateDbUrl
                user = dbUsername
                password = dbPassword
            }
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                includes = "public.*"
                excludes = "flyway_schema_history"
                inputSchema = "public"
                isOutputSchemaToDefault = true
            }
            target {
                directory = dbJooqOutputDirectory.asFile.absolutePath
                packageName = "com.example"
            }
        }
    }
}
