package com.app.mehdichouag.epitechintranet.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.fragment.TokenFragment;
import com.app.mehdichouag.epitechintranet.model.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.TreeSet;

public class PlanningAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<TaskModel> mData = new ArrayList<TaskModel>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private Context mContext;

    private LayoutInflater mInflater;

    public PlanningAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final TaskModel item) {
        mData.add(item);
    }

    public void addSectionHeaderItem(final TaskModel item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public TaskModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.field_planning, null);
                    holder.mStartTime = (TextView) convertView.findViewById(R.id.task_start_time);
                    holder.mDuration = (TextView) convertView.findViewById(R.id.task_duration);
                    holder.mTitle = (TextView) convertView.findViewById(R.id.task_title);
                    holder.mPlace = (TextView) convertView.findViewById(R.id.task_place);
                    holder.mToken = (Button) convertView.findViewById(R.id.validate_token);
                    holder.mRegistered = (ImageView) convertView.findViewById(R.id.is_registered);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.header_planning, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
            }
            if (convertView != null) {
                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TaskModel task = mData.get(position);
        if (holder.textView != null) {
            holder.textView.setText(task.getTitleHeader());
        } else if (holder.mStartTime != null) {
            holder.mStartTime.setText(task.getTimeStart());
            holder.mDuration.setText(task.getDuration());
            holder.mTitle.setText(task.getTitle());
            if (task.getRoom() != null) {
                holder.mPlace.setText(task.getRoom());
            } else {
                holder.mPlace.setText("Indéfinie");
            }
            if (task.isAllowToken()) {
                holder.mToken.setVisibility(View.VISIBLE);
                holder.mToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TokenFragment fragment = new TokenFragment();
                        Bundle b = new Bundle();
                        b.putSerializable(TokenFragment.TASK_KEY, task);
                        fragment.setArguments(b);
                        fragment.show(((FragmentActivity) mContext).getSupportFragmentManager(), null);
                    }
                });
            }
            if (task.getRegistered().equals("null")) {
                holder.mRegistered.setBackgroundResource(R.drawable.unregistered);
            } else {
                if (convertView != null) {
                    convertView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            addEventToCalendar(task, true);
                            return false;
                        }
                    });
                }
            }
        }
        return convertView;
    }

    private void addEventToCalendar(TaskModel task, boolean needReminder) {
        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1);
        eventValues.put("title", task.getTitle());
        eventValues.put("description", task.getTitleHeader());
        if (!task.getRoom().equals("null")) {
            eventValues.put("eventLocation", task.getRoom());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(task.getDuration());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);

        long endDate = task.getDate().getTime() + (1000 * 60 * 60 * calendar.get(Calendar.HOUR)) +
                (1000 * 60 * 60 * calendar.get(Calendar.MINUTE)); // For next 1hr

        eventValues.put("dtstart", task.getDate().getTime());
        eventValues.put("dtend", endDate);
        eventValues.put("allDay", 0);
        eventValues.put("eventStatus", 1);

        eventValues.put("hasAlarm", 1);

        String timeZone = TimeZone.getDefault().getID();
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

        Uri eventUri = mContext.getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            String reminderUriString = "content://com.android.calendar/reminders";
            ContentValues reminderValues = new ContentValues();
            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5);
            reminderValues.put("method", 1);
            Uri reminderUri = mContext.getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        }
        Toast.makeText(mContext, mContext.getResources().getString(R.string.event_added), Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder {
        public TextView textView;
        public TextView mStartTime;
        public TextView mDuration;
        public TextView mTitle;
        public TextView mPlace;
        public Button mToken;
        public ImageView mRegistered;
    }

}
