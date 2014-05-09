package com.jlwapps.mobiquitychallenge.app;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class PictureDialogFragment extends DialogFragment{

    public static String TAG = "PictureDialogFragment";

    private ImageView mThumbnailView;
    private Button mDropBoxButton;
    private Button mLocalButton;
    private Button mCancelButton;

    private Uri mPictureLocation;

    public static PictureDialogFragment newInstance(Uri pictureLocation)
    {
        PictureDialogFragment dialog = new PictureDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable("picture", pictureLocation);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPictureLocation = getArguments().getParcelable("picture");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_picture, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mThumbnailView = (ImageView) rootView.findViewById(R.id.thumbnail);
        mDropBoxButton = (Button) rootView.findViewById(R.id.btn_dropbox_save);
        mLocalButton = (Button) rootView.findViewById(R.id.btn_local_save);
        mCancelButton = (Button) rootView.findViewById(R.id.btn_cancel);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(mPictureLocation));
            if (bitmap != null) {
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 150,150, true);
                bitmap.recycle();
                if (newBitmap != null)
                    mThumbnailView.setImageBitmap(newBitmap);
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        mDropBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureUtils.savePictureToDropbox();
                dismiss();
            }
        });

        mLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureUtils.savePictureToLocal();
                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }
}
