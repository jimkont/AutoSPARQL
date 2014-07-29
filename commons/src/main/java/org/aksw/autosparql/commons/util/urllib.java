package org.aksw.autosparql.commons.util;

/**
 * The Java version of URLLib
 */

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

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

    public static StringBuffer HttpPostRequest(String path, String params){
        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(params);
            wr.flush();
            StringBuffer sb = new StringBuffer();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            wr.close();
            rd.close();
            return sb;
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
}
