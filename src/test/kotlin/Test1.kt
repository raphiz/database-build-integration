import com.example.public.Public.Companion.PUBLIC
import com.example.public.tables.records.CustomersRecord
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import kotlin.test.assertEquals


@ExtendWith(DbSetupExtension::class)
class Test1 {

    @Test
    fun test(databaseParameters: DatabaseInitializer) = databaseParameters.withDSL { dsl ->

        assertEquals(0, dsl.selectFrom(PUBLIC.CUSTOMERS).count())

        dsl.insertInto(PUBLIC.CUSTOMERS).set(
            CustomersRecord(
                id = null,
                firstName = "Max",
                lastName = "Mustermann",
                email = "max@hotmail.com",
                phone = "1234567890",
                createdAt = LocalDateTime.now(),
                status = "?"
            )
        ).execute()

        assertEquals(1, dsl.selectFrom(PUBLIC.CUSTOMERS).count())

    }
}
