package mehta.com.sunshine_scratch.data;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Weather forecast app test suite.
 *
 * Created by Ishan on 2016-03-08.
 */
public class AppTestSuite  extends TestSuite{

    public static Test Suite()
    {
        return new TestSuiteBuilder(AppTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public AppTestSuite()
    {
        super();
    }
}
