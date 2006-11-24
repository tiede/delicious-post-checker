// Copyright 2006 Kim Bjørn Tiedemann (www.tiede.dk/blog.tiede.dk) 
// Licensed under the Apache License, Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//   http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
// See the License for the specific language governing permissions and 
// limitations under the License.

/*
 * LinkCheckerTest.java
 * JUnit based test
 *
 * Created on 30. marts 2006, 10:34
 */

package dk.tiede.linkchecker;

import junit.framework.*;
import java.net.HttpURLConnection;

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
