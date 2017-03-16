package work.assisjrs.seleniumtestcase;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.*;


/**
 * Created by assis on 15/03/17.
 */
public class SeleniumTestCaseListener implements TestExecutionListener {
    private static final Class<?>[] CHAIN = { SeleniumListener.class, DBUnitListener.class };

    private List<TestExecutionListener> chain;

    private List<TestExecutionListener> reverseChain;

    public SeleniumTestCaseListener() {
        this.chain = createChain();
        this.reverseChain = new ArrayList<>(this.chain);
        Collections.reverse(this.reverseChain);
    }

    private List<TestExecutionListener> createChain() {
        try {
            List<TestExecutionListener> chain = new ArrayList<TestExecutionListener>(CHAIN.length);
            for (int i = 0; i < CHAIN.length; i++) {
                chain.add((TestExecutionListener) CHAIN[i].newInstance());
            }
            return chain;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create chain for classes " + Arrays.asList(CHAIN), ex);
        }
    }

    public void beforeTestClass(final TestContext testContext) throws Exception {
        forwards(listener -> listener.beforeTestClass(testContext));
    }

    public void prepareTestInstance(final TestContext testContext) throws Exception {
        forwards(listener -> listener.prepareTestInstance(testContext));
    }

    public void beforeTestMethod(final TestContext testContext) throws Exception {
        forwards(listener -> listener.beforeTestMethod(testContext));
    }

    public void afterTestMethod(final TestContext testContext) throws Exception {
        backwards(listener -> listener.afterTestMethod(testContext));
    }

    public void afterTestClass(final TestContext testContext) throws Exception {
        backwards(listener -> listener.afterTestClass(testContext));
    }

    private void forwards(Call call) throws Exception {
        runChain(this.chain.iterator(), call);
    }

    private void backwards(Call call) throws Exception {
        runChain(this.reverseChain.iterator(), call);
    }

    private void runChain(Iterator<TestExecutionListener> iterator, Call call) throws Exception {
        Throwable lastException = null;
        while (iterator.hasNext()) {
            try {
                call.call(iterator.next());
            } catch (Throwable ex) {
                lastException = ex;
            }
        }
        if (lastException != null) {
            if (lastException instanceof Exception) {
                throw (Exception) lastException;
            }
            throw new Exception(lastException);
        }
    }

    private interface Call {
        void call(TestExecutionListener listener) throws Exception;
    }
}
