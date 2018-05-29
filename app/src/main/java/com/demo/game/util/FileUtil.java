package com.demo.game.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

    public static String readAssetToString(InputStream stream) throws IOException {
        StringBuilder buf = new StringBuilder();
        BufferedReader in = new BufferedReader((new InputStreamReader(stream)));
        String str;
        while((str = in.readLine()) != null) buf.append("\n" + str);

        in.close();
        return buf.toString();
    }

}
