package com.jlwapps.mobiquitychallenge.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.jlwapps.mobiquitychallenge.app.DropBoxFragmentInterface;
import com.jlwapps.mobiquitychallenge.app.DropBoxInterface;
import com.jlwapps.mobiquitychallenge.app.ImageAdapter;
import com.jlwapps.mobiquitychallenge.app.R;
import com.jlwapps.mobiquitychallenge.app.asynctasks.DropboxLoadThumbnailTask;

import java.util.ArrayList;


/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class MyPicsFragment extends Fragment implements DropboxLoadThumbnailTask.DropboxLoadThumbnailInterface,
        DropBoxFragmentInterface{

    private DropBoxInterface mDBI;
    private GridView mPictureGrid;
    private ImageView mNoPicturesImage;
    private TextView mNoPicturesText;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDBI = (DropBoxInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement DropboxInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mypics, container, false);
        mPictureGrid = (GridView) rootView.findViewById(R.id.grid_pictures);
        mNoPicturesImage = (ImageView) rootView.findViewById(R.id.img_no_pictures);
        mNoPicturesText = (TextView) rootView.findViewById(R.id.lbl_no_pictures);

        mPictureGrid.setAdapter(new ImageAdapter(getActivity(), new ArrayList<String>(), new ArrayList<Bitmap>()));
        dropboxSyncFragment(mDBI.getEntries());

        setGridVisibility(mPictureGrid.getAdapter().getCount()<= 0);
        return rootView;
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

    @Override
    public void thumbnailLoaded(String filename, Bitmap thumbnail) {
        ((ImageAdapter)mPictureGrid.getAdapter()).addImage(filename, thumbnail);
        mPictureGrid.invalidateViews();
        setGridVisibility(mPictureGrid.getAdapter().getCount()<= 0);

    }

    @Override
    public void dropboxSyncFragment(DropboxAPI.Entry entries) {
        if(entries != null) {
            for (DropboxAPI.Entry entry : mDBI.getEntries().contents) {
                new DropboxLoadThumbnailTask(getActivity(), mDBI, this, entry).execute();
            }
        }
    }
}
