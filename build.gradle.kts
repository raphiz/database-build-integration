plugins {
    kotlin
    spring
    database
}

dependencies {
    implementation("org.jooq:jooq:3.19.11")
    implementation("org.postgresql:postgresql:42.7.4")

    implementation("org.flywaydb:flyway-core:10.17.2")
    implementation("org.flywaydb:flyway-database-postgresql:10.17.2")

    implementation("org.springframework.boot:spring-boot-starter-jooq:3.3.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.3")

    testImplementation(kotlin("test"))
}
