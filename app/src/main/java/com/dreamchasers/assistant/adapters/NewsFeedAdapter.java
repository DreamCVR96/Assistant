package com.dreamchasers.assistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.models.Album;
import com.dreamchasers.assistant.models.Reminder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macpro on 12/28/16.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>{

    private Context context;
    private List<Reminder> reminderList;
    private List<Album> albumList;

    @Override
    public NewsFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notification_title) TextView title;
        @BindView(R.id.notification_time) TextView time;
        @BindView(R.id.notification_content) TextView content;
        @BindView(R.id.header_separator) TextView textSeparator;
        @BindView(R.id.notification_icon) ImageView icon;
        @BindView(R.id.notification_circle) ImageView circle;
        private View view;

        public ViewHolder(final View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }

    public NewsFeedAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
