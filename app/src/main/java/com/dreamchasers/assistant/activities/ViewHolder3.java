package com.dreamchasers.assistant.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.adapters.ComplexRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macpro on 1/24/17.
 */

public class ViewHolder3 extends RecyclerView.ViewHolder {




    @BindView(R.id.thumbnail) ImageView thumbnail;
    @BindView(R.id.fbmeTitle) TextView TextView;

    private View view;

    public ViewHolder3(final View view) {
        super(view);
        this.view = view;

        ButterKnife.bind(view);
    }
    public ImageView getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        this.thumbnail = thumbnail;
    }


}
