package com.sdmemorycleaner;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by diego on 13/05/17.
 */

public class FileUtils {

    private static final long KB = 1024;
    private static final long MB = 1024*1024;
    private static final long GB = 1024*1024*1024;

    public static File[] getDirectories(String path) {
        return new File(path).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size=f.length();
        }
        return size;
    }

    public static String getFolderSizeString(File file) {
        String value;
        long size = getFolderSize(file);
        if(size >= GB) {
            value = size/GB + " Gb";
        } else if(size >= MB) {
            value = size/MB + " Mb";
        } else if(size >= KB) {
            value = size/KB + " Kb";
        } else {
            value = size + " bytes";
        }
        return value;
    }

}
