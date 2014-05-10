package com.jlwapps.mobiquitychallenge.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class DropboxImageAdapter extends BaseAdapter {

    private ArrayList<DbxFileInfo> pictures;
    private Context mContext;
    private final LayoutInflater mInflater;


    public DropboxImageAdapter(Context context, ArrayList<DbxFileInfo> pictures) {
        mContext = context;
        this.pictures = pictures;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Object getItem(int position) {
        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.gridview_item, null);

        SquareImageView thumbnail = (SquareImageView) convertView.findViewById(R.id.picture);
        TextView title = (TextView) convertView.findViewById(R.id.text);

        Bitmap bitmap;
        try {
            DbxFileSystem fileSystem = DbxFileSystem.forAccount(DbxAccountManager.getInstance(mContext.getApplicationContext(),
                    DropBoxConstants.APP_KEY, DropBoxConstants.APP_SECRET).getLinkedAccount());
            DbxFile dropboxThumbnail = fileSystem.openThumbnail(pictures.get(position).path, DbxFileSystem.ThumbSize.M, DbxFileSystem.ThumbFormat.PNG);
            FileInputStream in = dropboxThumbnail.getReadStream();
            bitmap = BitmapFactory.decodeStream(in);
            thumbnail.setImageBitmap(bitmap);
            in.close();
            dropboxThumbnail.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        title.setText(pictures.get(position).path.toString());

        return convertView;
    }


}
