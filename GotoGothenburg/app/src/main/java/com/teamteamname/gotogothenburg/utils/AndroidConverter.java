package com.teamteamname.gotogothenburg.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by kakan on 2015-10-02.
 */
public class AndroidConverter {

    /**
     * Provides the resoruceID of the given file, provided it is located in res/raw.
     * @param context the context which holds the resources.
     * @param file the file which to return the resourceID of.
     * @return the resoruceID if the given file.
     */
    public static int fileToRawResourceID(Context context, File file) {
        String fileName = file.getName();
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null (in fileToResoruceID)");
        }
        if (file == null) {
            throw new IllegalArgumentException("File must not be null (in fileToResoruceID)");
        }

        int index = fileName.lastIndexOf(".");
        return context.getResources().getIdentifier(fileName.substring(0, index), "raw", context.getPackageName());
    }
}
