package ch.yvu.teststore.plugin

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock


import static org.mockito.Mockito.when
import static org.mockito.MockitoAnnotations.initMocks
import static org.junit.Assert.assertEquals
import static ch.yvu.teststore.plugin.ScmChanges.ScmChange

class ScmChangesTest {

    @Mock
    private FileJsonProvider jsonProvider

    private ScmChanges scmChanges

    @Before
    void setUp() {
        initMocks(this);
        scmChanges = new ScmChanges(jsonProvider: jsonProvider)
    }

    @Test
    void canReadRevision() {
        when(jsonProvider.get()).thenReturn('[{"node": "abcd"}]')

        List<ScmChange> changes = scmChanges.getChanges()

        assertEquals("abcd", changes.get(0).revision)
    }

    @Test
    void canReadAuthor() {
        when(jsonProvider.get()).thenReturn('[{"user": "Yves Bonjour"}]')

        List<ScmChange> changes = scmChanges.getChanges()

        assertEquals("Yves Bonjour", changes.get(0).author)
    }

    @Test
    void canReadDescription() {
        when(jsonProvider.get()).thenReturn('[{"desc": "Changes the world"}]')

        List<ScmChange> changes = scmChanges.getChanges()

        assertEquals("Changes the world", changes.get(0).description)
    }

    @Test
    void canReadDate() {
        when(jsonProvider.get()).thenReturn('[{"date": [1484740451, -3600]}]')

        List<ScmChange> changes = scmChanges.getChanges()

        Date expectedDate = new Date((1484740451L + 3600L) * 1000L);
        assertEquals(expectedDate, changes.get(0).date)

    }
}
