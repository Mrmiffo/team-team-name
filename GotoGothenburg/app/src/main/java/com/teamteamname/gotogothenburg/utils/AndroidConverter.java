package com.teamteamname.gotogothenburg.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kakan on 2015-10-02.
 */
public final class AndroidConverter {

    private AndroidConverter() {}

    /**
     * Provides the resoruceID of the given file, provided it is located in res/raw.
     * @param context the context which holds the resources.
     * @param file the file which to return the resourceID of.
     * @return the resoruceID if the given file.
     */
    public static int fileToRawResourceID(Context context, File file) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null (in fileToResoruceID)");
        }
        if (file == null) {
            throw new IllegalArgumentException("File must not be null (in fileToResoruceID)");
        }
        final String fileName = file.getName();
        final int index = fileName.lastIndexOf('.');
        return context.getResources().getIdentifier(fileName.substring(0, index), "raw", context.getPackageName());
    }

    /**
     * Converts the contents of the given file to a String.
     * @param context the context which holds the assets.
     * @param file the file which to read.
     * @return the contents of the file as a String.
     */
    public static String fileToMessageConverter(Context context, File file) {
        final AssetManager assetManager = context.getAssets();
        final StringBuilder text = new StringBuilder();
        try {
            final Scanner scanner = new Scanner(assetManager.open(file.getName()), "UTF-8");
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.toString());
        } catch (IOException e) {
            Log.e("IoException", e.toString());
        }
        return text.toString();
    }
}
