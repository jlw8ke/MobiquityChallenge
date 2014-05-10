package com.jlwapps.mobiquitychallenge.app;

import com.dropbox.client2.DropboxAPI;

/**
 * Created by jlw8k_000 on 5/10/2014.
 */
public interface DropBoxFragmentInterface {
    public void dropboxSyncFragment(DropboxAPI.Entry entries);
}
