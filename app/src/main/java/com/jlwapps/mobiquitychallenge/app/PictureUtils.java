package com.jlwapps.mobiquitychallenge.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class PictureUtils {

    public static void savePictureToDropbox()
    {

    }

    public static void savePictureToLocal()
    {

    }

    public static File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }


}
