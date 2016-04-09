package ch.yvu.teststore.plugin

import org.junit.Test

import java.util.regex.Pattern

import static org.junit.Assert.assertEquals

class FileWalkerTest {

    private static String TEST_RESULTS_DIR = './src/test/data/test-results'
    private static Pattern PATTERN_FILE = ~/.*\/test.xml/
    private static Pattern PATTERN_NONE = ~/none/
    private static Pattern PATTERN_RECURSIVE = ~/.*recursive.*/


    @Test
    public void visitsFileMatchingPattern() {

        def fileWalker = new FileWalker(baseDir: TEST_RESULTS_DIR, pattern: PATTERN_FILE)

        def fileContents = []
        fileWalker.walkFileContents({ fileName, fileContent ->
            fileContents += fileContent
        })

        assertEquals(1, fileContents.size())
        assertEquals('My File content', fileContents[0])
    }

    @Test
    public void doesNotVisitFileWithNonMatchingPattern() {
        def fileWalker = new FileWalker(baseDir: TEST_RESULTS_DIR, pattern: PATTERN_NONE)

        def fileContents = []
        fileWalker.walkFileContents({fileName, fileContent ->
            fileContents += fileContent
        })

        assertEquals(0, fileContents.size())
    }

    @Test
    public void visitsFilesRecursively() {
        def fileWalker = new FileWalker(baseDir: TEST_RESULTS_DIR, pattern: PATTERN_RECURSIVE)

        def fileContents = []
        fileWalker.walkFileContents({fileName, fileContent ->
            fileContents += fileContent
        })

        assertEquals(1, fileContents.size())
        assertEquals('My recursive file content', fileContents[0])
    }
}
