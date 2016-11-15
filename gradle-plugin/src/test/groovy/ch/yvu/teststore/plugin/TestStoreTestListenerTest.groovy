package ch.yvu.teststore.plugin

import org.gradle.api.internal.tasks.testing.DefaultTestMethodDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

import static java.util.UUID.randomUUID
import static org.gradle.api.tasks.testing.TestResult.ResultType.*
import static org.mockito.ArgumentMatchers.*
import static org.mockito.Mockito.*
import static org.mockito.MockitoAnnotations.initMocks

class TestStoreTestListenerTest {

    private static final TEST_DESCRIPTOR_1 = new DefaultTestMethodDescriptor(randomUUID(), "MyClass", "myTest1")
    private static final TEST_DESCRIPTOR_2 = new DefaultTestMethodDescriptor(randomUUID(), "MyClass", "myTest2")
    private static final RUN_ID = randomUUID();

    @Mock
    private TeststoreClient client;

    @Mock
    private TeststoreClientFactory factory;

    @Mock
    private TestStoreTestOutputListener testOutputListener;

    private TestStorePluginExtension extension;

    private TestStoreTestListener listener;

    @Before
    public void setUp() {
        initMocks(this)
        when(client.createRun(anyString(), any(Date.class))).thenReturn(RUN_ID)
        when(factory.createClient()).thenReturn(client)
        extension = new TestStorePluginExtension()
        extension.revision = "abc-123"

        listener = new TestStoreTestListener(extension, factory, testOutputListener)
    }

    @Test
    public void createsRunIfItIsFirstTest() {
        extension.incremental = true

        listener.beforeTest(TEST_DESCRIPTOR_1)

        verify(client).createRun(eq(extension.revision), any(Date.class))
    }

    @Test
    public void onlyCreatesRunOnce() {
        extension.incremental = true

        listener.beforeTest(TEST_DESCRIPTOR_1)
        listener.beforeTest(TEST_DESCRIPTOR_2)

        verify(client).createRun(eq(extension.revision), any(Date.class))
    }

    @Test
    public void doesNotCreateRunIfIncrementalIsNotSet() {
        extension.incremental = false

        listener.beforeTest(TEST_DESCRIPTOR_1)

        verify(client, never()).createRun(anyString(), any(Date.class))
    }

    @Test
    public void storesResultWhenTestFailed() {
        extension.incremental = true

        Throwable e = new AssertionError("failure")
        TestResult result = mock(TestResult.class)
        when(result.startTime).thenReturn(1L)
        when(result.endTime).thenReturn(10L)
        when(result.resultType).thenReturn(FAILURE)
        when(result.exception).thenReturn(e)
        when(testOutputListener.getLogOutput(TEST_DESCRIPTOR_1)).thenReturn("logMessage")

        listener.beforeTest(TEST_DESCRIPTOR_1)
        listener.afterTest(TEST_DESCRIPTOR_1, result)

        verify(client).insertTestResult(
                eq(RUN_ID),
                eq("MyClass#myTest1"),
                eq(false),
                eq(9L),
                any(Date.class),
                eq(stackTrace(e)),
                eq("logMessage"))
    }

    @Test
    public void storesResultWhenTestPassed() {
        extension.incremental = true

        TestResult result = mock(TestResult.class)
        when(result.startTime).thenReturn(1L)
        when(result.endTime).thenReturn(10L)
        when(result.resultType).thenReturn(SUCCESS)
        when(testOutputListener.getLogOutput(TEST_DESCRIPTOR_1)).thenReturn("")

        listener.beforeTest(TEST_DESCRIPTOR_1)
        listener.afterTest(TEST_DESCRIPTOR_1, result)

        verify(client).insertTestResult(
                eq(RUN_ID),
                eq("MyClass#myTest1"),
                eq(true),
                eq(9L),
                any(Date.class),
                eq(""),
                eq(""))
    }

    @Test
    public void doesNotStoreResultWhenTestSkipped() {
        extension.incremental = true

        TestResult result = mock(TestResult.class)
        when(result.resultType).thenReturn(SKIPPED)

        listener.beforeTest(TEST_DESCRIPTOR_1)
        listener.afterTest(TEST_DESCRIPTOR_1, result)

        verify(client, never()).insertTestResult(any(UUID.class), anyString(), anyBoolean(), anyLong(), any(Date.class), anyString(), anyString())
    }

    @Test
    public void doesNOtStoreResultsWhenIncrementalIsNotSet() {
        extension.incremental = false

        TestResult result = mock(TestResult.class)
        when(result.resultType).thenReturn(SUCCESS)

        listener.beforeTest(TEST_DESCRIPTOR_1)
        listener.afterTest(TEST_DESCRIPTOR_1, result)

        verifyZeroInteractions(client)
    }

    private static String stackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }



}
