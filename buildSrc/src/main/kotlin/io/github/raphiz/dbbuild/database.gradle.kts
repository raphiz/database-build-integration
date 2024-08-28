plugins {
    kotlin
    id("org.flywaydb.flyway")
    id("org.jooq.jooq-codegen-gradle")
}

// Configuration of the database connection parameters
val dbUrl = "jdbc:postgresql://localhost:5432/"
val dbUser = "localdev"
val dbPassword = "*****"
val dbTemplateName = "template1"
val templateDbUrl = "$dbUrl$dbTemplateName"

val jooqGeneratedCode =
    layout.buildDirectory.dir("generated/jooq").map { it.asFile.apply { mkdirs() } } as Provider<File>
val flywayLocation = layout.projectDirectory.dir("src/main/resources/db/migration")

flyway {
    url = templateDbUrl
    user = dbUser
    password = dbPassword
    locations = arrayOf("filesystem:${flywayLocation.asFile.absoluteFile}")
    cleanOnValidationError = true
    cleanDisabled = false
}

// Add jOOQ generated code to the main source set
sourceSets {
    main {
        java.srcDirs(jooqGeneratedCode)
    }
}
tasks.compileKotlin.configure {
    dependsOn(tasks.jooqCodegen)
}

dependencies {
    jooqCodegen("org.postgresql:postgresql:42.7.4")
}

tasks.test {
    dependsOn(tasks.flywayMigrate)
    environment("DB_URL", dbUrl)
    environment("DB_USER", dbUser)
    environment("DB_PASSWORD", dbPassword)
    environment("DB_TEMPLATE_NAME", "template1")
    systemProperty("org.jooq.no-logo", "true")
    systemProperty("org.jooq.no-tips", "true")
}

tasks.jooqCodegen.configure {
    // db must be migrated before jooq can generate the code.
    // The jOOQ code mostly depends on the flyway locations and can be cached when the migrations have not changed
    dependsOn(tasks.flywayMigrate)
    inputs.files(flywayLocation)
}

jooq {
    configuration {
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            jdbc {
                driver = "org.postgresql.Driver"
                url = templateDbUrl
                user = dbUser
                password = dbPassword
            }
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                includes = "public.*"
                excludes = "flyway_schema_history"
            }
            target {
                directory = jooqGeneratedCode.get().absolutePath
                packageName = "com.example"
            }
        }
    }
}
