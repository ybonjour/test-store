package ch.yvu.teststore.config


import ch.yvu.teststore.result.CassandraResultRepository
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.CassandraRunRepository
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.testsuite.CassandraTestSuiteRepository
import ch.yvu.teststore.testsuite.TestSuiteRepository
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment

@Configuration
@PropertySource("classpath:META-INF/cassandra.properties")
open class CassandraConfig {

    // Constructor injection is not possible, since configuration classes need a
    // default constructor
    @Autowired lateinit var environment: Environment

    @Autowired lateinit var session: Session


    @Bean open fun session(): Session {
        val cluster = Cluster.builder()
                .addContactPoint(environment.getRequiredProperty("cassandra.contactPoints"))
                .withPort(environment.getRequiredProperty("cassandra.port", Int::class.java))
                .build()
        return cluster.connect(environment.getRequiredProperty("cassandra.keyspace"))
    }

    @Bean open fun testSuiteRepository(): TestSuiteRepository {
        return CassandraTestSuiteRepository(session)
    }

    @Bean open fun runRepository(): RunRepository {
        return CassandraRunRepository(session)
    }

    @Bean open fun resultRepository(): ResultRepository {
        return CassandraResultRepository(session)
    }
}

