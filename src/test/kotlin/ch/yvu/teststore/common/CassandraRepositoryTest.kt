package ch.yvu.teststore.common

import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import com.datastax.driver.mapping.annotations.Table
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks


class CassandraRepositoryTest {
    companion object {
        val TABLE_NAME = "myTable"
        val ITEM = MyModel("item")
    }

    @Mock
    lateinit var mappingManager: MappingManager

    @Mock
    lateinit var session: Session

    @Mock
    lateinit var mapper: Mapper<MyModel>

    lateinit var repository: MyRepository

    @Before fun setUp() {
        initMocks(this)
        mappingManager = mock(MappingManager::class.java)
        `when`(mappingManager.session).thenReturn(session)
        `when`(mappingManager.mapper(MyModel::class.java)).thenReturn(mapper)

        repository = MyRepository(mappingManager)
    }

    @Test fun canSaveAnItem() {
        repository.save(ITEM)

        verify(mapper).save(ITEM);
    }


    @Table(name = "myTable")
    data class MyModel(var name: String) : Model

    class MyRepository(mappingManager: MappingManager) : CassandraRepository<MyModel>(mappingManager, TABLE_NAME, MyModel::class.java)
}