package com.jlwapps.mobiquitychallenge.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class PictureUtils {

    public interface SavePicturesInterface
    {
        public void onLocalSaveComplete(boolean success);
    }

    public static void savePictureToDropbox(Context context, File file, String title)
    {
        DbxAccountManager manager = DbxAccountManager.getInstance(context.getApplicationContext(),
                DropBoxConstants.APP_KEY, DropBoxConstants.APP_SECRET);
        try {
            DbxFileSystem dbxFS = DbxFileSystem.forAccount(manager.getLinkedAccount());
            DbxFile dropboxFile = dbxFS.create(new DbxPath(title));
            FileInputStream in = new FileInputStream(file);
            FileOutputStream out = dropboxFile.getWriteStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            dropboxFile.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void savePictureToLocal(Context context, SavePicturesInterface spi, Uri loc, String title)
    {
        Log.i("p", context.getFilesDir().toString());
        boolean success = false;
        try {
            InputStream in = context.getContentResolver().openInputStream(loc);
            FileOutputStream out = context.openFileOutput(title, Context.MODE_PRIVATE);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            success = true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        spi.onLocalSaveComplete(success);
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
