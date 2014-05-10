package com.jlwapps.mobiquitychallenge.app.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jlw8k_000 on 5/10/2014.
 */
public class DropboxLoadAllTask extends AsyncTask<Void, Long, List<DbxFileInfo>> {
    public interface DropboxLoadAllInterface {
        public void onFinishDropboxLoad(List<DbxFileInfo> images);
    }

    private Context context;
    private DbxFileSystem fileSystem;
    private DropboxLoadAllInterface mInterface;

    public DropboxLoadAllTask(Context context, DbxFileSystem fileSystem, DropboxLoadAllInterface mInterface)
    {
        this.context  =context;
        this.fileSystem = fileSystem;
        this.mInterface = mInterface;
    }
    @Override
    protected List<DbxFileInfo> doInBackground(Void... params) {
        List<DbxFileInfo> images;
        try {
            images = fileSystem.listFolder(DbxPath.ROOT);
            return images;
        } catch (Exception e ){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<DbxFileInfo> dbxFileInfos) {
        mInterface.onFinishDropboxLoad(dbxFileInfos);
    }
}
