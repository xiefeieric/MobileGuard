package com.fei.mobileguard.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Fei on 15/09/15.
 */
public class StreamUtil {

    public static String streamToString(InputStream is) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int length = 0;
        byte[] buffer = new byte[1024];
        while ((length = is.read(buffer))!=-1) {
            bos.write(buffer,0,length);
        }
        String result = bos.toString();
        is.close();
        bos.close();
        return result;
    }
}
