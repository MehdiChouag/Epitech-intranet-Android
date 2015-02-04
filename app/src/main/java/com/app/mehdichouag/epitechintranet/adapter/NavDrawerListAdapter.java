package com.app.mehdichouag.epitechintranet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.model.NavDrawerItem;

import java.util.List;

/**
 * Created by mehdichouag on 05/01/15.
 */
public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {

    private static class ViewHolder {
        TextView title;
        ImageView icon;
    }

    private int mRessource;
    private List<NavDrawerItem> mItems;

    public NavDrawerListAdapter(Context context, int resource, List<NavDrawerItem> objects) {
        super(context, resource, objects);
        this.mItems = objects;
        this.mRessource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavDrawerItem item = mItems.get(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_drawer, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(item.getTitle());
        holder.icon.setImageResource(item.getIcon());

        return convertView;
    }
}
