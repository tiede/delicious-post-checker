/*
 * LinkCheckerTest.java
 * JUnit based test
 *
 * Created on 30. marts 2006, 10:34
 */

package dk.tiede.linkchecker;

import junit.framework.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author kbt
 */
public class LinkCheckerTest extends TestCase {
    
    public LinkCheckerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LinkCheckerTest.class);
        
        return suite;
    }

    /**
     * Test of checkLink method, of class dk.tiede.linkchecker.LinkChecker.
     */
    public void testCheckLink() throws Exception {
        String theUrl = "http://blog.tiede.dk";
        
        int expResult = HttpURLConnection.HTTP_OK;
        LinkChecker.Result result = LinkChecker.checkLink(theUrl);
        assertEquals(expResult, result.getResponseCode());
        assertFalse(result.isIoError());
    }
    
    public void testCheckLinkError() throws Exception {
        String theUrl = "http://www.jshfjsgfjdsgf.dk";
        LinkChecker.Result result = LinkChecker.checkLink(theUrl);
        assertTrue(result.isIoError());
    }
    
}
