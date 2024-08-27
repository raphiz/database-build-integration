import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.time.Duration

fun DatabaseInitializer.withDSL(block: (dsl: DSLContext) -> Unit) =
    DSL.using(url, username, password).use { dsl ->
        block(dsl)
    }


fun Connection.execute(query: String, timeout: Duration = Duration.ofSeconds(5)) {
    this.createStatement().use {
        it.queryTimeout = timeout.toSeconds().toInt()
        it.execute(query)
    }
}

fun <R> Connection.query(
    query: String,
    timeout: Duration = Duration.ofSeconds(5),
    mapper: (ResultSet) -> R
): R =
    this.createStatement().use {
        it.queryTimeout = timeout.toSeconds().toInt()
        return mapper(it.executeQuery(query))
    }

fun <R> withConnection(
    databaseParameters: DatabaseInitializer, block: Connection.() -> R
): R {
    DriverManager.getConnection(databaseParameters.url, databaseParameters.username, databaseParameters.password).use {
        return block(it)
    }
}


