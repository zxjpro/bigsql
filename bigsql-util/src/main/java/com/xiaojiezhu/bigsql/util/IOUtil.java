package com.xiaojiezhu.bigsql.util;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xiaojie.zhu
 */
public class IOUtil {

    public static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeable = null;
            }
        }
    }

    public static void close(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }

    public static String toString(InputStream in) throws IOException {
        return toString(in,null);
    }

    public static String toString(InputStream in,LineHandler lineHandler) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null){
            if(lineHandler != null){
                lineHandler.onLine(line);
            }
            sb.append(line);
        }
        return sb.toString();
    }

    public static String toString(File file) throws IOException {
        InputStream in = null;
        String s;
        try {
            in = new FileInputStream(file);
            s = toString(in);
        } finally {
            close(in);
        }
        return s;
    }



    public static interface LineHandler{
        void onLine(String line);
    }
}
