package com.jlwapps.mobiquitychallenge.app.NavigationDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlwapps.mobiquitychallenge.app.R;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 5/7/2014.
 */
public class NavDrawerAdapter extends BaseAdapter {

    private ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
    private final Context mContext;
    private final LayoutInflater mInflater;

    public NavDrawerAdapter(Context context, ArrayList<NavDrawerItem> items)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Only remake view if it has not been made before
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        icon.setImageResource(items.get(position).getIcon());
        title.setText(items.get(position).getTitle());
        return convertView;
    }
}
