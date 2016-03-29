package ch.yvu.teststore.config


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean
import org.springframework.data.cassandra.config.SchemaAction.NONE
import org.springframework.data.cassandra.convert.MappingCassandraConverter
import org.springframework.data.cassandra.core.CassandraOperations
import org.springframework.data.cassandra.core.CassandraTemplate
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@Configuration
@EnableCassandraRepositories
@PropertySource("classpath:META-INF/cassandra.properties")
open class CassandraConfig {

    // Constructor injection is not possible, since configuration classes need a
    // default constructor
    @Autowired lateinit var environment: Environment

    @Bean
    open fun cluster(): CassandraClusterFactoryBean {
        val cluster = CassandraClusterFactoryBean()

        cluster.setContactPoints(environment.getRequiredProperty("cassandra.contactPoints"))
        cluster.setPort(environment.getRequiredProperty("cassandra.port", Int::class.java))
        return cluster
    }

    @Bean
    open fun session(): CassandraSessionFactoryBean {
        val session = CassandraSessionFactoryBean()
        session.setCluster(cluster().`object`)
        session.setKeyspaceName(environment.getRequiredProperty("cassandra.keyspace"))
        session.converter = MappingCassandraConverter(BasicCassandraMappingContext())
        session.schemaAction = NONE

        return session
    }

    @Bean
    open fun cassandraTemplate(): CassandraOperations {
        return CassandraTemplate(session().getObject());
    }
}

