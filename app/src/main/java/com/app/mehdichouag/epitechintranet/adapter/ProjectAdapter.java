package com.app.mehdichouag.epitechintranet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.model.Project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mehdichouag on 31/01/15.
 */
public class ProjectAdapter extends ArrayAdapter<Project> {

    private class ViewHolder {
        TextView module;
        TextView project;
        TextView end;

        public ViewHolder(View rootView) {
            module = (TextView) rootView.findViewById(R.id.module_name);
            project = (TextView) rootView.findViewById(R.id.project_name);
            end = (TextView) rootView.findViewById(R.id.end_time);
        }
    }

    private List<Project> mProjectList;

    public ProjectAdapter(Context context, List<Project> objects) {
        super(context, R.layout.item_list_project, objects);
        mProjectList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        Project project = mProjectList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_project, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.project.setText(project.getProjectName());
        holder.module.setText(project.getModuleName());

        holder.end.setText(getDate(project.getProjectEnd()));

        return convertView;
    }

    private String getDate(String end) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String DATE_FORMAT_NOW = "EEEE dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(date);
    }

    @Override
    public int getCount() {
        return (mProjectList != null) ? mProjectList.size() : 0;
    }
}
