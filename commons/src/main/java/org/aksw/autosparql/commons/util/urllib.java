package org.aksw.autosparql.commons.util;

/**
 * The Java version of URLLib
 */

import java.net.*;
import java.io.*;

public class URLlib {
    public static StringBuffer urlopen(String url) {
        try {
            URL u = new URL(url);
            StringBuffer sb = new StringBuffer();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(u.openStream()));
            String str = null;
            while((str = in.readLine())!=null) {
                sb.append(str);
            }
            in.close();
            return sb;
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
