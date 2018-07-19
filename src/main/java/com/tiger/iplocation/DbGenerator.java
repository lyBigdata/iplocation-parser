package com.tiger.iplocation;

import java.io.File;
import java.io.IOException;

/**
 * com.tiger.iplocation.DbGenerator
 *
 * @author: zhaolihe
 * @email: dayingzhaolihe@126.com
 * @date 2018-07-16 13:05:00
 */
public class DbGenerator {

    public static void main(String[] args) {
        String str = DbGenerator.class.getResource("/").getPath();
        String sourceFile = DbGenerator.class.getResource("/ip.merge.txt").getFile();
        if (str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        try {
            String fileName = String.format("%s/ip2region.db", str);
            File file = new File(fileName);
            if(file.exists()){
                file.delete();
            }

            DbConfig localDbConfig = new DbConfig();
            DbMaker localDbMaker = new DbMaker(localDbConfig, sourceFile);
            localDbMaker.make(fileName);
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }
}
