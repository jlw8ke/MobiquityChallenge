package com.jlwapps.mobiquitychallenge.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class BottomActionBarFragment extends Fragment {

    private ImageButton cameraButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actionbar_lower, container, false);
        return rootView;
    }
}
