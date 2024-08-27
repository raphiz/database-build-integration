buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.17.2")
    }
}

plugins {
    kotlin("jvm") version "2.0.20"
    id("org.flywaydb.flyway") version "10.17.2"
    id("org.jooq.jooq-codegen-gradle") version "3.19.11"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.flywaydb:flyway-database-postgresql:10.17.2")

    implementation("org.jooq:jooq:3.19.11")

    implementation("org.postgresql:postgresql:42.7.4")
    jooqCodegen("org.postgresql:postgresql:42.7.4")
}

kotlin {
    jvmToolchain(21)
}


val dbUrl by extra("jdbc:postgresql://localhost:5432/")
val dbUser by extra("localdev")
val dbPassword by extra("*****")
val dbTemplateName by extra("template1")
val templateDbUrl by extra { "$dbUrl$dbTemplateName" }

val jooqGeneratedCode = layout.buildDirectory.map { it.dir("generated/jooq").asFile.apply { mkdirs() } }
val flywayLocation = project.layout.projectDirectory.dir("src/main/resources/db/migration")

sourceSets {
    main {
        java.srcDirs(jooqGeneratedCode)
    }
}

flyway {
    url = templateDbUrl
    user = dbUser
    password = dbPassword
    locations = arrayOf("filesystem:${flywayLocation.asFile.absoluteFile}")
    cleanOnValidationError = true
    cleanDisabled = false
}

tasks.flywayMigrate.configure {
    inputs.files(flywayLocation)
}

tasks.test {
    dependsOn(tasks.flywayMigrate)
    environment("DB_URL", dbUrl)
    environment("DB_USER", dbUser)
    environment("DB_PASSWORD", dbPassword)
    environment("DB_TEMPLATE_NAME", "template1")

    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}


tasks.jooqCodegen.configure {
    dependsOn(tasks.flywayMigrate)
    inputs.files(flywayLocation)
}
tasks.compileKotlin.configure {
    dependsOn(tasks.jooqCodegen)
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
            generate {
//                isGeneratedAnnotation = false
//                isDaos = false
//                isDaos = true
                isPojosAsKotlinDataClasses = true
            }
            strategy {
                //
            }
            target {
                directory = jooqGeneratedCode.get().toString()
                packageName = "com.example"
            }
        }
    }
}