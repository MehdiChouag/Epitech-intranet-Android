package com.app.mehdichouag.epitechintranet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.callback.TrombiClickListener;
import com.app.mehdichouag.epitechintranet.model.Trombi;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mehdichouag on 01/02/15.
 */
public class TrombiAdapter extends RecyclerView.Adapter<TrombiAdapter.TrombiViewHolder> {

    private List<Trombi> mTrombiList;
    private Context mContext;
    private TrombiClickListener mLongListener;

    public TrombiAdapter(List<Trombi> list, Context ctx, TrombiClickListener listener) {
        this.mTrombiList = list;
        mContext = ctx;
        mLongListener= listener;
    }

    @Override
    public TrombiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_trombi_list, parent, false);
        return new TrombiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrombiViewHolder holder, int position) {

        if (!mTrombiList.isEmpty() && mTrombiList.get(position) != null) {
            Trombi trombi = mTrombiList.get(position);

            Picasso.with(mContext).load(mContext.getString(R.string.api_user_picture, trombi.getLogin())).placeholder(R.drawable.user_placeholder).into(holder.picture);
            holder.login.setText(trombi.getLogin());
            holder.position = position;
        }
    }

    @Override
    public int getItemCount() {
        return mTrombiList != null ? mTrombiList.size() : 0 ;
    }

    public void setTrombi(List<Trombi> list) {
        this.mTrombiList = list;
        notifyDataSetChanged();
    }

    public class TrombiViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{

        ImageView picture;
        TextView login;
        int position;

        public TrombiViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            picture = (ImageView) itemView.findViewById(R.id.trombi_picture);
            login = (TextView) itemView.findViewById(R.id.login);
        }

        @Override
        public void onClick(View v) {
            if (mLongListener != null)
                mLongListener.OnClickTrombi(position, picture);
        }
    }
}
