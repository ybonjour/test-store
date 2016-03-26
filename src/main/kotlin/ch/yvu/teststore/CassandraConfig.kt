package ch.yvu.teststore

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
open class CassandraConfig {

    @Bean
    open fun cluster(): CassandraClusterFactoryBean {
        val cluster = CassandraClusterFactoryBean();
        val contactPoints:String = "192.168.99.100"
        val port:Int = 9042
        cluster.setContactPoints(contactPoints)
        cluster.setPort(port)
        return cluster
    }

    @Bean
    open fun session(): CassandraSessionFactoryBean {
        val session = CassandraSessionFactoryBean()
        val keyspaceName = "teststore"
        session.setCluster(cluster().`object`)
        session.setKeyspaceName(keyspaceName)
        session.converter = MappingCassandraConverter(BasicCassandraMappingContext())
        session.schemaAction = NONE

        return session
    }

    @Bean
    open fun cassandraTemplate(): CassandraOperations {
        return CassandraTemplate(session().getObject());
    }
}

