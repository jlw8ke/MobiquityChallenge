package com.jlwapps.mobiquitychallenge.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jlwapps.mobiquitychallenge.app.MainActivity;
import com.jlwapps.mobiquitychallenge.app.PictureUtils;
import com.jlwapps.mobiquitychallenge.app.R;
import com.jlwapps.mobiquitychallenge.app.dialogs.PictureDialogFragment;

import java.io.File;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class BottomActionBarFragment extends Fragment {

    private ImageButton cameraButton;
    private Uri mImageLocation;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actionbar_lower, container, false);

        cameraButton = (ImageButton) rootView.findViewById(R.id.btn_camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //Check there is a camera activity to handle event
                if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
                {
                    try
                    {
                        File tempPhoto = PictureUtils.createTemporaryFile("temp", ".png");
                        mImageLocation = Uri.fromFile(tempPhoto);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageLocation);
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }catch (Exception e)
                    {
                        Log.v("Create Picture Error", "Can't create file to take picture!");
                        Toast.makeText(getActivity(),
                                "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == MainActivity.RESULT_OK)
                {
                    PictureDialogFragment dialog = PictureDialogFragment.newInstance(mImageLocation);
                    dialog.show(getFragmentManager(), PictureDialogFragment.TAG);
                }
        }
    }
}
