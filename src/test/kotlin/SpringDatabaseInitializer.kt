import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent
import org.springframework.core.env.MapPropertySource
import java.io.Closeable
import javax.sql.DataSource

class SpringDatabaseInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {

        // It is also be possible to get/modify the existing jdbc params instead of getting them via env as follows:
        // applicationContext.environment.getProperty("spring.datasource.url")
        val initializer = DatabaseInitializer(
            baseUrl = System.getenv("DB_URL"),
            username = System.getenv("DB_USER"),
            password = System.getenv("DB_PASSWORD"),
            templateDbName = System.getenv("DB_TEMPLATE_NAME"),
            testDbName = "testdb_${System.getProperty("org.gradle.test.worker")}"
        )

        initializer.initialize()
        val propertySource = MapPropertySource(
            "DatabaseInitializer Override", mapOf(
                "spring.datasource.url" to initializer.url,
                "spring.datasource.username" to initializer.username,
                "spring.datasource.password" to initializer.password,
            )
        )
        applicationContext.environment.propertySources.addFirst(propertySource)

        applicationContext.addApplicationListener {
            if (it is ContextClosedEvent) {
                val dataSource = it.applicationContext.getBean<DataSource>()
                if (dataSource is Closeable) {
                    dataSource.close()
                }
                initializer.close()
            }
        }

    }
}