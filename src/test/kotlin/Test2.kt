import io.github.raphiz.dbbuild.CustomerRepository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class Test2 {

    @Test
    fun test() = withDatabase { dsl ->
        val repository = CustomerRepository(dsl)
        assertEquals(0, repository.count())

        repository.insertRandomCustomer()

        assertEquals(1, repository.count())
    }

    @Test
    fun test2() = withDatabase { dsl ->
        val repository = CustomerRepository(dsl)
        assertEquals(0, repository.count())

        repository.insertRandomCustomer()

        assertEquals(1, repository.count())
    }
}
