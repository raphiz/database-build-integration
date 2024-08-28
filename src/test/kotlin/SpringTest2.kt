import io.github.raphiz.dbbuild.Application
import io.github.raphiz.dbbuild.CustomerRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals


@ExtendWith(SpringExtension::class)
@ContextConfiguration(
    classes = [Application::class],
    initializers = [SpringDatabaseInitializer::class]
)
@Transactional // use transaction for each test
class SpringTest2 {
    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Test
    fun test1() {
        customerRepository.insertRandomCustomer()
        assertEquals(1, customerRepository.count())
    }

    @Test
    fun test2() {
        customerRepository.insertRandomCustomer()
        assertEquals(1, customerRepository.count())
    }
}
