package io.github.raphiz.dbbuild

import com.example.public.Public.Companion.PUBLIC
import com.example.public.tables.records.CustomersRecord
import org.jooq.DSLContext
import org.springframework.beans.factory.getBean
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Service
class CustomerRepository(private val dsl: DSLContext) {
    fun insertRandomCustomer() {
        dsl.insertInto(PUBLIC.CUSTOMERS).set(
            CustomersRecord(
                id = null,
                firstName = "Max",
                lastName = "Mustermann",
                email = "${UUID.randomUUID()}@hotmail.com",
                phone = "1234567890",
                createdAt = LocalDateTime.now(),
                status = "?"
            )
        ).execute()
    }

    fun count() = dsl.fetchCount(PUBLIC.CUSTOMERS)
}

@SpringBootApplication
class Application(private val applicationContext: ApplicationContext) : CommandLineRunner {

    @Bean
    fun customerRepository(): CustomerRepository = CustomerRepository(applicationContext.getBean())

    override fun run(vararg args: String?) {
        val customerRepository = applicationContext.getBean<CustomerRepository>()
        customerRepository.insertRandomCustomer()
        println("Counting Customers: ${customerRepository.count()}")
    }

}