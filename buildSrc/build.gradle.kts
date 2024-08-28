plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.0.20")
    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:2.0.20")
    implementation("org.jooq.jooq-codegen-gradle:org.jooq.jooq-codegen-gradle.gradle.plugin:3.19.11")
    implementation("org.jooq:jooq-meta:3.19.11")

    implementation("org.flywaydb:flyway-core:10.17.2")
    implementation("org.flywaydb:flyway-database-postgresql:10.17.2")
    implementation("org.postgresql:postgresql:42.7.4")
}
