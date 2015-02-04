package com.app.mehdichouag.epitechintranet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.model.Marks;

import java.util.List;

/**
 * Created by mehdichouag on 19/01/15.
 */
public class MarksAdapter extends ArrayAdapter<Marks> {

    public static class ViewHolder{
        TextView projectName;
        TextView marks;
    }

    private List<Marks> mListMarks;
    private Context mContext;

    public MarksAdapter(Context context, List<Marks> objects) {
        super(context, R.layout.item_list_marks, objects);
        this.mListMarks = objects;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Marks currentMarks = mListMarks.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_marks, parent, false);
            holder.projectName = (TextView) convertView.findViewById(R.id.project_name);
            holder.marks = (TextView) convertView.findViewById(R.id.marks);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final double marks = currentMarks.getMark();
        if (marks == Math.floor(marks))
            holder.marks.setText(String.valueOf((int)marks));
        else
            holder.marks.setText(String.valueOf(marks));
        holder.marks.setTextColor((marks > 1.5) ?  mContext.getResources().getColor(R.color.greenMarks) : mContext.getResources().getColor(R.color.redMarks));
        holder.projectName.setText(currentMarks.getProjectName());
        return convertView;
    }

    public List<Marks> getMarks() {
        return mListMarks;
    }
}
