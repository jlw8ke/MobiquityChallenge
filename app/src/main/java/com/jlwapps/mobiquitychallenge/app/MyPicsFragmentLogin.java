package com.jlwapps.mobiquitychallenge.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

/**
 * Created by jlw8k_000 on 5/8/2014.
 */
public class MyPicsFragmentLogin extends Fragment {

    public interface MyPicsFragmentInterface
    {
        public DropboxAPI<AndroidAuthSession> getDropboxAPI();

    }

    private ImageButton mDropBoxButton;
    private TextView mDropBoxLabel;
    private MyPicsFragmentInterface mInterface;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (MyPicsFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement DropboxFileInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mypics_nologin, container, false);

        mDropBoxButton = (ImageButton) rootView.findViewById(R.id.dropbox_btn);
        mDropBoxLabel = (TextView) rootView.findViewById(R.id.dropbox_label);

        mDropBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.getDropboxAPI().getSession().startOAuth2Authentication(getActivity());
            }
        });

        return rootView;
    }
}
