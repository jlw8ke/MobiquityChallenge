package com.jlwapps.mobiquitychallenge.app.dialogs;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jlwapps.mobiquitychallenge.app.DropBoxInterface;
import com.jlwapps.mobiquitychallenge.app.PictureUtils;
import com.jlwapps.mobiquitychallenge.app.R;
import com.jlwapps.mobiquitychallenge.app.asynctasks.DropboxUploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class PictureDialogFragment extends DialogFragment implements PictureUtils.SavePicturesInterface{

    public static String TAG = "PictureDialogFragment";

    private ImageView mThumbnailView;
    private EditText mTitleView;
    private Button mDropBoxButton;
    private Button mLocalButton;
    private Button mCancelButton;
    private TextView mCreationDate;

    private Uri mPictureLocation;
    private PictureUtils.SavePicturesInterface mInterface;

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
        mInterface = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_picture, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mTitleView = (EditText) rootView.findViewById(R.id.picture_title);
        mThumbnailView = (ImageView) rootView.findViewById(R.id.thumbnail);
        mDropBoxButton = (Button) rootView.findViewById(R.id.btn_dropbox_save);
        mLocalButton = (Button) rootView.findViewById(R.id.btn_local_save);
        mCancelButton = (Button) rootView.findViewById(R.id.btn_cancel);
        mCreationDate = (TextView) rootView.findViewById(R.id.creationDate);

        Date currentDate = new Date();
        mTitleView.setText(UUID.randomUUID().toString());
        mCreationDate.setText(currentDate.toString());

        //Loading image thumbnail preview
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
                Map<Uri, String> filesToUpload = new HashMap<Uri, String>();
                filesToUpload.put(mPictureLocation, mTitleView.getText().toString() + ".png");
                DropboxUploadTask uploadTask = new DropboxUploadTask(getActivity(), null, filesToUpload, ((DropBoxInterface)getActivity()).getDropboxAPI());
                uploadTask.execute();
                dismiss();
            }
        });

        mLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PictureUtils.savePictureToLocal(
                            getActivity(),
                            mInterface ,
                            mPictureLocation,
                            mTitleView.getText().toString());

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


    @Override
    public void onLocalSaveComplete(boolean success) {
        if(success)
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.local_save_success), Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.save_fail), Toast.LENGTH_SHORT).show();
        }
    }
}
