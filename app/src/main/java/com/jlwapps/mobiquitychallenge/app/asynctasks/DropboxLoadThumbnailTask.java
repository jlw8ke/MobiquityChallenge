package com.jlwapps.mobiquitychallenge.app.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.jlwapps.mobiquitychallenge.app.DropBoxInterface;
import com.jlwapps.mobiquitychallenge.app.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by jlw8k_000 on 5/10/2014.
 */
public class DropboxLoadThumbnailTask extends AsyncTask<Void, Long, Bitmap> {

    public interface DropboxLoadThumbnailInterface
    {
        public void thumbnailLoaded(String filename, Bitmap thumbnail);
    }

    private DropBoxInterface dbi;
    private DropboxLoadThumbnailInterface dblthni;
    private Context mContext;
    private DropboxAPI.Entry entry;
    private String mErrorMsg;
    private String mFilename;

    public DropboxLoadThumbnailTask(Context context, DropBoxInterface dbi, DropboxLoadThumbnailInterface dblthni, DropboxAPI.Entry entry) {
        this.mContext = context;
        this.dbi = dbi;
        this.dblthni = dblthni;
        this.entry = entry;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        if(entry.isDir || entry.isDeleted || !entry.thumbExists)
            return null;
        else
        {
            mFilename = entry.fileName();
            String thumbnailPath = mContext.getCacheDir().getAbsolutePath() + "/" + mFilename;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(thumbnailPath);
            } catch (FileNotFoundException e) {
                mErrorMsg = mContext.getString(R.string.save_fail);
                return null;
            }
            try {
                dbi.getDropboxAPI().getThumbnail(entry.path, out, DropboxAPI.ThumbSize.BESTFIT_320x240, DropboxAPI.ThumbFormat.PNG, null);
                Bitmap thumbnail = BitmapFactory.decodeFile(thumbnailPath);
                return thumbnail;
            } catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap == null)
            Toast.makeText(mContext, mErrorMsg, Toast.LENGTH_SHORT).show();
        else
            dblthni.thumbnailLoaded(mFilename, bitmap);
    }
}
