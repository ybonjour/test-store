package ch.yvu.teststore.plugin

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestOutputEvent
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class TestStoreTestOutputListenerTest {

    private TestStoreTestOutputListener testOutputListener;

    @Before
    public void setUp() {
        testOutputListener = new TestStoreTestOutputListener()
    }

    @Test
    public void storesLogOutputForTest() {
        def testDescriptor = testDescriptor("MyClass", "myTest")
        def event = testOutputEvent("A log message")

        testOutputListener.onOutput(testDescriptor, event)
        String logOutput = testOutputListener.getLogOutput(testDescriptor)

        assertEquals("A log message", logOutput)
    }

    @Test
    public void returnsEmptyStringWhenNoLogMessageForTest() {
        def testDescriptor = testDescriptor("MyClass", "myTest")

        String logOutput = testOutputListener.getLogOutput(testDescriptor)

        assertEquals("", logOutput)
    }

    @Test
    public void removesLogWhenRetrieved() {
        def testDescriptor = testDescriptor("MyClass", "myTest")
        def event = testOutputEvent("A log message")

        testOutputListener.onOutput(testDescriptor, event)
        testOutputListener.getLogOutput(testDescriptor)
        String logOutput = testOutputListener.getLogOutput(testDescriptor)

        assertEquals("", logOutput)
    }


    private static TestOutputEvent testOutputEvent(String message){
        def testOutputEvent = mock(TestOutputEvent.class)
        when(testOutputEvent.getMessage()).thenReturn(message)

        return testOutputEvent
    }

    private static TestDescriptor testDescriptor(String className, String name) {
        def testDescriptor = mock(TestDescriptor.class)
        when(testDescriptor.className).thenReturn(className)
        when(testDescriptor.name).thenReturn(name)
        return testDescriptor
    }
}
