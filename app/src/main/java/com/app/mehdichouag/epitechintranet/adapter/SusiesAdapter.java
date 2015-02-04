package com.app.mehdichouag.epitechintranet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.activity.DetailActivity;
import com.app.mehdichouag.epitechintranet.model.Susie;

import java.util.List;

/**
 * Created by Flave on 12/01/15.
 */
public class SusiesAdapter extends RecyclerView.Adapter<SusiesAdapter.ViewHolder> {
    private List<Susie> mSusieList;
    private Activity mActivity;

    public SusiesAdapter(List<Susie> susieList, Activity activity) {
        this.mSusieList = susieList;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_susies_recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (!mSusieList.isEmpty() && mSusieList.get(i) != null) {
            Susie susie = mSusieList.get(i);
            viewHolder.title.setText(susie.getTitle());
            viewHolder.susieName.setText(susie.getSusieName());
            viewHolder.date.setText(susie.getStartDate());
            viewHolder.type.setText(susie.getType().toString());
            switch (susie.getType()) {
                case FUN:
                    viewHolder.imageView.setBackgroundResource(R.drawable.fun);
                    break;
                case READING:
                    viewHolder.imageView.setImageResource(R.drawable.reading);
                    break;
                case CULTURE:
                    viewHolder.imageView.setImageResource(R.drawable.culture);
                    break;
            }
            viewHolder.setSusie(susie);
            viewHolder.activity = mActivity;
        }
    }

    public void setSusieList(List<Susie> susieList) {
        mSusieList = susieList;
    }

    @Override
    public int getItemCount() {
        if (mSusieList == null) {
            return 0;
        }
        return mSusieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        /* TAGS */
        public final static String TAG_SUSIE = "susie";

        /* PUBLIC VARS */
        public ImageView imageView;
        public TextView title;
        public TextView type;
        public TextView susieName;
        public TextView date;
        public Activity activity;

        /* PRIVATE VARS */
        private Susie mSusie;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.susie_imageView);
            title = (TextView) itemView.findViewById(R.id.susie_title);
            type = (TextView) itemView.findViewById(R.id.susie_type);
            susieName = (TextView) itemView.findViewById(R.id.susie_name);
            date = (TextView) itemView.findViewById(R.id.susie_date);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra(TAG_SUSIE, mSusie);
            activity.startActivity(intent);
        }

        public void setSusie(Susie mSusie) {
            this.mSusie = mSusie;
        }
    }
}
