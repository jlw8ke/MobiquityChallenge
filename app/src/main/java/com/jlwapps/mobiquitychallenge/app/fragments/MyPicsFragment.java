package com.jlwapps.mobiquitychallenge.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jlwapps.mobiquitychallenge.app.DropBoxInterface;
import com.jlwapps.mobiquitychallenge.app.R;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class MyPicsFragment extends Fragment {

    private DropBoxInterface mDBI;
    private GridView mPictureGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mypics, container, false);
        mPictureGrid = (GridView) rootView.findViewById(R.id.grid_pictures);
        return rootView;
    }
}
