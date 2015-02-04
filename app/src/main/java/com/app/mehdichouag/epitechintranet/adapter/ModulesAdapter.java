package com.app.mehdichouag.epitechintranet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.model.Modules;

import java.util.List;

/**
 * Created by mehdichouag on 28/01/15.
 */
public class ModulesAdapter extends ArrayAdapter<Modules> {

    List<Modules> mModulesList;

    private class ViewHolder {
        TextView grade;
        TextView credit;
        TextView title;

        public ViewHolder(View rootView) {
            grade = (TextView) rootView.findViewById(R.id.grade);
            credit = (TextView) rootView.findViewById(R.id.credit);
            title = (TextView) rootView.findViewById(R.id.module_title);
        }
    }

    public ModulesAdapter(Context context, List<Modules> objects) {
        super(context, R.layout.item_list_modules, objects);
        mModulesList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String GRADE_FAIL = "echec";

        ViewHolder holder;

        Modules modules = mModulesList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_modules, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final String grade = modules.getGrade();

        holder.title.setText(modules.getTitle());
        holder.grade.setText(grade);
        holder.credit.setText(String.valueOf(modules.getCredit()));

        if (!grade.equals("-")) {
            if (!grade.equalsIgnoreCase(GRADE_FAIL))
                holder.grade.setTextColor(getContext().getResources().getColor(R.color.greenMarks));
            else
                holder.grade.setTextColor(getContext().getResources().getColor(R.color.redMarks));
        }
        return convertView;
    }

    public void setModulesList(List<Modules> modules) {
        this.mModulesList = modules;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mModulesList != null) ? mModulesList.size() : 0;
    }
}
