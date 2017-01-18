package ch.yvu.teststore.plugin

import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

class FileJsonProviderTest {

    private static final String JSON_FILE = "src/test/data/changes/file.json"
    private static final String JSON_FILE_CONTENT = "{ \"foo\": \"bar\" }"


    @Test
    void returnsFileContent() {
        def jsonProvider = new FileJsonProvider(filePath: JSON_FILE)

        String content = jsonProvider.get()

        assertEquals(JSON_FILE_CONTENT, content)
    }

    @Test
    void returnsNullIfFileDoesNotExist() {
        def jsonProvider = new FileJsonProvider(filePath: "somePath/to/nonExisting/file.json")

        String content = jsonProvider.get()

        assertNull(content)
    }

    @Test
    void returnsNullIfFilePathIsNull() {
        def jsonProvider = new FileJsonProvider(filePath: null)

        String content = jsonProvider.get()

        assertNull(content)
    }
}
