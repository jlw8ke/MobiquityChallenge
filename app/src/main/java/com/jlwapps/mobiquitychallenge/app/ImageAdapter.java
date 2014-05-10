package com.jlwapps.mobiquitychallenge.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 5/9/2014.
 */
public class ImageAdapter extends BaseAdapter {

    private ArrayList<String> imageNames;
    private ArrayList<Bitmap> images;
    private Context mContext;
    private final LayoutInflater mInflater;


    public ImageAdapter(Context c, ArrayList<String> imageNames, ArrayList<Bitmap> images) {
        mContext = c;
        this.imageNames = imageNames;
        this.images = images;
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
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

        thumbnail.setImageBitmap(images.get(position));
        title.setText(imageNames.get(position));

        return convertView;
    }

    public void addImage(String title, Bitmap bitmap)
    {
        imageNames.add(title);
        images.add(bitmap);
    }

    public void removeImage(int position)
    {
        imageNames.remove(position);
        images.remove(position);
    }

}
