import com.example.tables.records.CustomersRecord
import com.example.tables.references.CUSTOMERS
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import kotlin.test.assertEquals


@ExtendWith(DbSetupExtension::class)
class Test3 {

    @Test
    fun test(databaseParameters: DatabaseInitializer) = databaseParameters.withDSL { dsl ->

        assertEquals(0, dsl.selectFrom(CUSTOMERS).count())

        dsl.insertInto(CUSTOMERS).set(
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

        assertEquals(1, dsl.selectFrom(CUSTOMERS).count())

    }
}
