package com.jlwapps.mobiquitychallenge.app.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.jlwapps.mobiquitychallenge.app.DropBoxConstants;
import com.jlwapps.mobiquitychallenge.app.ImageAdapter;
import com.jlwapps.mobiquitychallenge.app.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class MyPicsFragment extends Fragment {

    private GridView mPictureGrid;
    private ImageView mNoPicturesImage;
    private TextView mNoPicturesText;
    private DbxAccountManager manager;

    private DbxFileSystem mFileSystem;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mypics, container, false);
        mPictureGrid = (GridView) rootView.findViewById(R.id.grid_pictures);
        mNoPicturesImage = (ImageView) rootView.findViewById(R.id.img_no_pictures);
        mNoPicturesText = (TextView) rootView.findViewById(R.id.lbl_no_pictures);

        manager = DbxAccountManager.getInstance(getActivity().getApplicationContext(),
                DropBoxConstants.APP_KEY, DropBoxConstants.APP_SECRET);


         return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mFileSystem = DbxFileSystem.forAccount(manager.getLinkedAccount());
            List<DbxFileInfo> infos = mFileSystem.listFolder(DbxPath.ROOT);
            loadPictures(infos);
            Log.i("PyPicsFragment", infos.toString());
            mFileSystem.addPathListener(new DbxFileSystem.PathListener() {
                @Override
                public void onPathChange(DbxFileSystem dbxFileSystem, DbxPath dbxPath, Mode mode) {
                    try {
                        List<DbxFileInfo> infos = mFileSystem.listFolder(DbxPath.ROOT);
                        loadPictures(infos);
                    }catch (Exception e){ e.printStackTrace();}
                }}, DbxPath.ROOT, DbxFileSystem.PathListener.Mode.PATH_OR_CHILD);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setGridVisibility(boolean isEmpty)
    {
        if(isEmpty) {
            mPictureGrid.setVisibility(View.GONE);
            mNoPicturesImage.setVisibility(View.VISIBLE);
            mNoPicturesText.setVisibility(View.VISIBLE);
        } else {
            mPictureGrid.setVisibility(View.VISIBLE);
            mNoPicturesImage.setVisibility(View.GONE);
            mNoPicturesText.setVisibility(View.GONE);
        }
    }

    private void loadPictures(List<DbxFileInfo> pictures)
    {
        mPictureGrid.setAdapter(new ImageAdapter(getActivity(), (ArrayList<DbxFileInfo>) pictures));
        setGridVisibility(mPictureGrid.getAdapter().isEmpty());
        mPictureGrid.invalidateViews();
    }





}
