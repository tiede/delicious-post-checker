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
 * LinkChecker.java
 *
 * Created on 30. marts 2006, 10:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dk.tiede.linkchecker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author kbt
 */
public class LinkChecker {
    
    private LinkChecker() {
    }
    
    public static Result checkLink(String theUrl) throws MalformedURLException {
        URL url = new URL(theUrl);
        HttpURLConnection urlConnection = null;
        
        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("GET");
            
            urlConnection.connect();
            
            return new Result(urlConnection.getResponseCode(), false);
        }
        catch (IOException e) {
            return new Result(0, true);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    } 
    
    public static class Result {
        private int responseCode = 0;
        private boolean ioError = false;
        
        public Result(int responseCode, boolean ioError) {
            this.responseCode = responseCode;
            this.ioError = ioError;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public boolean isIoError() {
            return ioError;
        }
        
        public boolean isSuccess() {
            return (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_FORBIDDEN)  && !isIoError();
        }
        
    } 
}
