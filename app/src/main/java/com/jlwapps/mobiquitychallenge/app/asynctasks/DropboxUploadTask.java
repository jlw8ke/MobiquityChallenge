package com.jlwapps.mobiquitychallenge.app.asynctasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import java.util.List;
import java.util.Map;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class DropboxUploadTask extends AsyncTask<Void, Long, Boolean> {

    private DropboxAPI<AndroidAuthSession> dbAPI;
    private Map<Uri, String> filesToUpload;
    private Context mContext;
    private DropboxUploadTaskInterface mInterface;

    @Override
    protected Boolean doInBackground(Void... params) {
        return null;
    }

    public interface DropboxUploadTaskInterface
    {

    }
}
