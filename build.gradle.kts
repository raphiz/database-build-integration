plugins {
    kotlin
    database
}

dependencies {
    implementation("org.jooq:jooq:3.19.11")
    implementation("org.postgresql:postgresql:42.7.4")

    testImplementation(kotlin("test"))
}
