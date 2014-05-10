package com.jlwapps.mobiquitychallenge.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


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
            Toast.makeText(context, context.getString(R.string.dropbox_save_success), Toast.LENGTH_SHORT).show();

        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.save_fail_dropbox), Toast.LENGTH_SHORT).show();
        }
    }

    public static void savePictureToLocal(Context context, File input, String title)
    {
        boolean success = false;
        try {
            FileInputStream in = new FileInputStream(input);
            FileOutputStream out = context.openFileOutput(title, Context.MODE_PRIVATE);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            Toast.makeText(context, context.getString(R.string.local_save_success), Toast.LENGTH_SHORT).show();
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.save_fail), Toast.LENGTH_SHORT).show();
        }
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
