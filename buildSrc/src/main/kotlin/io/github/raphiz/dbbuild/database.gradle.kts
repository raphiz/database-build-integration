import io.github.raphiz.dbbuild.DatabaseExtension
import io.github.raphiz.dbbuild.FlywayMigrateTask

plugins {
    kotlin
    id("org.jooq.jooq-codegen-gradle")
}

val db = extensions.create<DatabaseExtension>("database").apply {
    jdbcUrl = "jdbc:postgresql://localhost:5432/"
    username = "localdev"
    password = "*****"
    templateDbName = "template1"
    jooqOutputDirectory = layout.buildDirectory.dir("generated/jooq").get().also { it.asFile }
    migrationsLocation = layout.projectDirectory.dir("src/main/resources/db/migration")
    templateDbUrl = "${jdbcUrl}${templateDbName}"
}

// Use custom FlywayMigrate task because the official flyway gradle plugin is badly maintained and not compatible with the Gradle Configuration Cache
// see https://github.com/flyway/flyway/issues/3550
val flywayMigrate by tasks.registering(FlywayMigrateTask::class) {
    url.convention(db.templateDbUrl)
    username.convention(db.username)
    password.convention(db.password)
    migrationsLocation = db.migrationsLocation
}

sourceSets {
    main {
        java.srcDirs(db.jooqOutputDirectory)
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
    environment("DB_URL", db.jdbcUrl)
    environment("DB_USER", db.username)
    environment("DB_PASSWORD", db.password)
    environment("DB_TEMPLATE_NAME", "template1")
    systemProperty("org.jooq.no-logo", "true")
    systemProperty("org.jooq.no-tips", "true")
}

tasks.jooqCodegen.configure {
    // db must be migrated before jooq can generate the code.
    // The jOOQ code mostly depends on the flyway locations and can be cached when the migrations have not changed
    dependsOn(flywayMigrate)
    inputs.files(db.migrationsLocation)
}

jooq {
    configuration {
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            jdbc {
                driver = "org.postgresql.Driver"
                url = db.templateDbUrl
                user = db.username
                password = db.password
            }
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                includes = "public.*"
                excludes = "flyway_schema_history"
                inputSchema = "public"
                isOutputSchemaToDefault = true
            }
            target {
                directory = db.jooqOutputDirectory.asFile.absolutePath
                packageName = "com.example"
            }
        }
    }
}
