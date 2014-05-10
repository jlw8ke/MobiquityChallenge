package com.jlwapps.mobiquitychallenge.app.asynctasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.jlwapps.mobiquitychallenge.app.DropBoxInterface;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by jlw8k_000 on 5/10/2014.
 */
public class DropboxLoadAllTask extends AsyncTask<Void, Long, DropboxAPI.Entry> {

    private DropBoxInterface dbi;
    private Context mContext;
    String path;

    public DropboxLoadAllTask(Context context, DropBoxInterface dbi, String path)
    {
        this.mContext = context;
        this.dbi = dbi;
        this.path = path;
    }

    @Override
    protected DropboxAPI.Entry doInBackground(Void... params) {
        DropboxAPI.Entry entries = null;
        try {
            entries = dbi.getDropboxAPI().metadata(path, 100, null, true, null);
        } catch (DropboxException e) {
            e.printStackTrace();
            return null;
        }
        Iterator<DropboxAPI.Entry> iter = entries.contents.iterator();
        while(iter.hasNext())
        {
            DropboxAPI.Entry e = iter.next();
            if (e.isDeleted) {
                iter.remove();
            }
        }
        return entries;
    }

    @Override
    protected void onPostExecute(DropboxAPI.Entry entries) {
        //super.onPostExecute(entries);
        dbi.dropboxSync(entries);

    }
}
