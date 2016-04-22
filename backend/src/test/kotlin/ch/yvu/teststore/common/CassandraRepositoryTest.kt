package ch.yvu.teststore.common

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import com.datastax.driver.mapping.Result
import com.datastax.driver.mapping.annotations.Table
import org.junit.Assert.assertEquals
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

    @Mock
    lateinit var result: Result<MyModel>

    lateinit var repository: MyRepository

    @Before fun setUp() {
        initMocks(this)
        `when`(mappingManager.session).thenReturn(session)
        `when`(mappingManager.mapper(MyModel::class.java)).thenReturn(mapper)

        repository = MyRepository(mappingManager)
    }

    @Test fun canSaveAnItem() {
        repository.save(ITEM)

        verify(mapper).save(ITEM);
    }

    @Test fun returnsSavedItem() {
        val result = repository.save(ITEM)
        assertEquals(ITEM, result)
    }

    @Test fun findAllSendsCorrectQuery() {
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString())).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(emptyList<MyModel>())

        repository.findAll()

        verify(session).execute("SELECT * FROM myTable")
    }

    @Test fun findAllReturnsCorrectResult() {
        val model = MyModel("Foo")
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString())).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(listOf(model))

        val result = repository.findAll()

        assertEquals(listOf(model), result)
    }

    @Test fun deleteAllSendsCorrectQuery() {
        repository.deleteAll()

        verify(mapper).deleteQuery("DELETE FROM myTable")
    }


    @Table(name = "myTable")
    data class MyModel(var name: String) : Model

    class MyRepository(mappingManager: MappingManager) : CassandraRepository<MyModel>(mappingManager, TABLE_NAME, MyModel::class.java)
}