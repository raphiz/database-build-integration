import org.junit.jupiter.api.extension.*
import java.sql.Connection
import java.sql.DriverManager
import java.time.Duration

class DbSetupExtension : BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private val databaseInitializer = DatabaseInitializer(
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

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type.isAssignableFrom(DatabaseInitializer::class.java)
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return databaseInitializer
    }
}

// TODO: Spring integration á la /home/raphiz/nextcloud/notebook/dev/Technologie/Datenbanken/Ansätze für DB Testing.md
class DatabaseInitializer(
    val baseUrl: String,
    val username: String,
    val password: String,
    private val templateDbName: String,
    private val testDbName: String
) : AutoCloseable {
    val url = "$baseUrl$testDbName"
    private val connection by lazy { DriverManager.getConnection(baseUrl, username, password) }

    fun initialize() {
        connection.execute(
            """
                DROP DATABASE IF EXISTS $testDbName;
                CREATE DATABASE $testDbName TEMPLATE $templateDbName;
            """.trimIndent()
        )
    }

    override fun close() {
        try {
            connection.execute("DROP DATABASE IF EXISTS $testDbName;")
        } finally {
            connection.close()
        }
    }

    private fun Connection.execute(query: String, timeout: Duration = Duration.ofSeconds(5)) {
        this.createStatement().use {
            it.queryTimeout = timeout.toSeconds().toInt()
            it.execute(query)
        }
    }
}
