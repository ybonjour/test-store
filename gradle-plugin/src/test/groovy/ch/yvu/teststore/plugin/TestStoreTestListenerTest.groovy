package ch.yvu.teststore.plugin

import org.gradle.api.internal.tasks.testing.DefaultTestMethodDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

import static java.util.UUID.randomUUID
import static org.gradle.api.tasks.testing.TestResult.ResultType.*
import static org.mockito.Mockito.*
import static org.mockito.MockitoAnnotations.initMocks

class TestStoreTestListenerTest {

    private static final TEST_DESCRIPTOR_1 = new DefaultTestMethodDescriptor(randomUUID(), "MyClass", "myTest1")
    private static final TEST_DESCRIPTOR_2 = new DefaultTestMethodDescriptor(randomUUID(), "MyClass", "myTest2")
    private static final RUN_ID = randomUUID();

    @Mock
    private TeststoreClient client;

    @Mock
    private TeststoreClientFactory clientFactory;

    @Mock
    private TestStoreTestOutputListener testOutputListener;

    @Mock
    private ScmChangesFactory scmChangesFactory;

    @Mock
    private ScmChanges scmChanges

    private TestStorePluginExtension extension;

    private TestStoreTestListener listener;

    @Before
    public void setUp() {
        initMocks(this)
        when(client.createRun(anyString(), any(Date.class))).thenReturn(RUN_ID)
        when(clientFactory.createClient()).thenReturn(client)
        when(scmChangesFactory.createScmChanges()).thenReturn(scmChanges)
        extension = new TestStorePluginExtension()
        extension.revision = "abc-123"

        listener = new TestStoreTestListener(extension, clientFactory, testOutputListener, scmChangesFactory)
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
    public void createsScmChangeForRun() {
        extension.incremental = true
        ScmChanges.ScmChange scmChange = new ScmChanges.ScmChange()
        when(scmChanges.getChanges()).thenReturn([scmChange])

        listener.beforeTest(TEST_DESCRIPTOR_1)

        verify(client).insertScmChange(RUN_ID, scmChange)
    }

    @Test
    public void onlyCreateScmChangesOnce() {
        extension.incremental = true
        ScmChanges.ScmChange scmChange = new ScmChanges.ScmChange()
        when(scmChanges.getChanges()).thenReturn([scmChange])

        listener.beforeTest(TEST_DESCRIPTOR_1)
        listener.beforeTest(TEST_DESCRIPTOR_2)

        verify(client).insertScmChange(RUN_ID, scmChange)
    }

    @Test
    public void createsMultipleScmChangeForRun() {
        extension.incremental = true
        ScmChanges.ScmChange scmChange1 = new ScmChanges.ScmChange()
        ScmChanges.ScmChange scmChange2 = new ScmChanges.ScmChange()
        when(scmChanges.getChanges()).thenReturn([scmChange1, scmChange2])

        listener.beforeTest(TEST_DESCRIPTOR_1)

        verify(client).insertScmChange(RUN_ID, scmChange1)
        verify(client).insertScmChange(RUN_ID, scmChange2)
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
    public void doesNotStoreLogWhenTestIsPassed() {
        extension.incremental = true

        TestResult result = mock(TestResult.class)
        when(result.resultType).thenReturn(SUCCESS)
        when(testOutputListener.getLogOutput(TEST_DESCRIPTOR_1)).thenReturn("Some log message")

        listener.beforeTest(TEST_DESCRIPTOR_1);
        listener.afterTest(TEST_DESCRIPTOR_1, result);

        verify(client).insertTestResult(
                any(UUID.class),
                anyString(),
                anyBoolean(),
                anyLong(),
                any(Date.class),
                anyString(), eq(""))
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
    public void doesNotStoreResultsWhenIncrementalIsNotSet() {
        extension.incremental = false

        TestResult result = mock(TestResult.class)
        when(result.resultType).thenReturn(SUCCESS)

        listener.beforeTest(TEST_DESCRIPTOR_1)
        listener.afterTest(TEST_DESCRIPTOR_1, result)

        verifyZeroInteractions(client)
    }

    @Test
    public void swallowsExceptionWhenInsertingTestResult() {
        extension.incremental = true
        doThrow(new RuntimeException()).when(client).insertTestResult(
                any(UUID.class),
                anyString(),
                anyBoolean(),
                anyLong(),
                any(Date.class),
                anyString(),
                anyString())
        TestResult result = mock(TestResult.class)
        when(result.resultType).thenReturn(SUCCESS)

        listener.beforeTest(TEST_DESCRIPTOR_1)
        listener.afterTest(TEST_DESCRIPTOR_1, result)

        // success if no exception has been thrown
    }

    private static String stackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
