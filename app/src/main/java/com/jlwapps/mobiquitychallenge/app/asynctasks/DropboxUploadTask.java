package com.jlwapps.mobiquitychallenge.app.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.jlwapps.mobiquitychallenge.app.DropBoxInterface;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class DropboxUploadTask extends AsyncTask<Void, Long, Void> {

    private DropboxAPI<AndroidAuthSession> dbAPI;
    private Map<Uri, String> filesToUpload;
    private Context mContext;
    private DropBoxInterface dbi;
    private DropboxAPI.UploadRequest mRequest;


    public DropboxUploadTask(Context context, DropBoxInterface dbi, Map<Uri, String> filesToUpload, DropboxAPI<AndroidAuthSession> dbAPI)
    {
        mContext = context.getApplicationContext();
        this.dbi = dbi;
        this.filesToUpload = filesToUpload;
        this.dbAPI = dbAPI;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try
        {
            for(Map.Entry<Uri, String> entry : filesToUpload.entrySet() )
            {
                File fileToUpload = new File(entry.getKey().getPath());
                FileInputStream in = new FileInputStream(fileToUpload);
                String uploadPath = "/" + entry.getValue();
                mRequest = dbAPI.putFileOverwriteRequest(uploadPath, in, fileToUpload.length(), null);
                if(mRequest != null)
                    mRequest.upload();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}
