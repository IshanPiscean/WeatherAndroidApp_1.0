package mehta.com.sunshine_scratch.data;

import android.test.AndroidTestCase;

/**
 * Created by Ishan on 2016-03-06.
 */
public class TestPractice extends AndroidTestCase {
    /*
        This gets run before every test.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    public void testThatDemonstratesAssertions() throws Throwable {
        int a = 5;
        int b = 30;
        int c = 5;
        int d = 10;

        assertEquals("X should be equal", a, c);
        assertTrue("Y should be true", d > a);
        assertFalse("Z should be false", a == b);

        if (b < d) {
            fail("XX should never happen");
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}