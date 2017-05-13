package com.sdmemorycleaner;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by diego on 13/05/17.
 */

public class FileUtils {

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/Android";

    private static final long KB = 1024;
    private static final long MB = 1024*1024;
    private static final long GB = 1024*1024*1024;

    public static ArrayList<File> getFolders(Context context) {
        ArrayList<File> folders = new ArrayList<>();
        File[] directories = getDirectories(ROOT_PATH);
        File[] subdirectories;
        for (File file: directories) {
            subdirectories = getDirectories(file.getPath());
            for (File subfile: subdirectories) {
                if (getFolderSize(subfile) != 0 && !isPackageInstalled(context, subfile.getName())) {
                    folders.add(subfile);
                }
            }
        }
        return folders;
    }

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

    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

}
