import org.junit.jupiter.api.extension.*
import java.sql.Connection
import java.sql.DriverManager
import java.time.Duration

class DbSetupExtension :
    BeforeEachCallback,
    AfterEachCallback,
    ParameterResolver {
    private val databaseInitializer =
        DatabaseInitializer(
            baseUrl = System.getenv("DB_URL"),
            username = System.getenv("DB_USER"),
            password = System.getenv("DB_PASSWORD"),
            templateDbName = System.getenv("DB_TEMPLATE_NAME"),
            testDbName = "testdb_${System.getProperty("org.gradle.test.worker")}",
        )

    override fun beforeEach(context: ExtensionContext?) {
        databaseInitializer.initialize()
    }

    override fun afterEach(context: ExtensionContext?) {
        databaseInitializer.close()
    }

    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
    ): Boolean = parameterContext.parameter.type.isAssignableFrom(DatabaseInitializer::class.java)

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
    ): Any = databaseInitializer
}

class DatabaseInitializer(
    baseUrl: String,
    val username: String,
    val password: String,
    private val templateDbName: String,
    private val testDbName: String,
) : AutoCloseable {
    val url = "$baseUrl$testDbName"
    private val connection by lazy { DriverManager.getConnection(baseUrl, username, password) }

    fun initialize() {
        connection.execute(
            """
            DROP DATABASE IF EXISTS $testDbName;
            CREATE DATABASE $testDbName TEMPLATE $templateDbName;
            """.trimIndent(),
        )
    }

    override fun close() {
        try {
            connection.execute("DROP DATABASE IF EXISTS $testDbName WITH (FORCE);")
        } finally {
            connection.close()
        }
    }

    private fun Connection.execute(
        query: String,
        timeout: Duration = Duration.ofSeconds(5),
    ) {
        this.createStatement().use {
            it.queryTimeout = timeout.toSeconds().toInt()
            it.execute(query)
        }
    }
}

// class SqliteDatabaseInitializer(
//    private val templateDb: Path,
//    val username: String,
//    val password: String,
//    testDbName: String,
// )  {
//    private val dbFile = Files.createTempFile(testDbName, ".db")
//
//    val url = "jdbc:sqlite::${dbFile.toAbsolutePath()}"
//
//    fun initialize() {
//        Files.copy(templateDb, dbFile)
//    }
//
//    fun close() {
//        Files.delete(dbFile)
//    }
// }
