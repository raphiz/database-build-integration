package io.github.raphiz.dbbuild

import org.gradle.api.file.Directory

interface DatabaseExtension {
    var jooqOutputDirectory: Directory
    var migrationsLocation: Directory
    var jdbcUrl: String
    var username: String
    var password: String
    var templateDbName: String
    var templateDbUrl: String
}