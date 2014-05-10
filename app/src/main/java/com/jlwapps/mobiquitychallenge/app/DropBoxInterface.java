package com.jlwapps.mobiquitychallenge.app;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public interface DropBoxInterface {
    public DropboxAPI<AndroidAuthSession> getDropboxAPI();
    public void dropboxSync(DropboxAPI.Entry entries);
    public DropboxAPI.Entry getEntries();
}
