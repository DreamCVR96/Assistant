package com.dreamchasers.assistant.adapters;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamchasers.assistant.R;

import com.dreamchasers.assistant.activities.MainActivity;
import com.dreamchasers.assistant.activities.ViewHolder2;
import com.dreamchasers.assistant.activities.ViewHolder3;
import com.dreamchasers.assistant.models.Album;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.onClick;
import static android.os.Build.VERSION_CODES.M;


/**
 * Created by macpro on 1/23/17.
 */

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;

    private final int IMAGE = 1, ALBUM = 2;

    // Provide a suitable constructor (depends on the kind of data set)
    public ComplexRecyclerViewAdapter(List<Object> items) {
        this.items = items;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return IMAGE;
        }
        else if (items.get(position) instanceof Album) {
            return ALBUM;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {

            case IMAGE:
                View v2 = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false);
                viewHolder = new ViewHolder2(v2);
                break;
            case ALBUM:
                View v3 = inflater.inflate(R.layout.album_card, viewGroup, false);
                viewHolder = new ViewHolder3(v3);
                break;

            default:
                View v4 = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false);
                viewHolder = new ViewHolder2(v4);
                break;
        }
        return viewHolder;
    }

    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the
     * RecyclerView.ViewHolder contents with the item at the given position
     * and also sets up some private fields to be used by RecyclerView.
     *
     * @param viewHolder The type of RecyclerView.ViewHolder to populate
     * @param position Item position in the viewgroup.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case IMAGE:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            case ALBUM:
                ViewHolder3 vh3 = (ViewHolder3) viewHolder;
                configureViewHolder3(vh3, position);
                break;
            default:
                ViewHolder2 vh4 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh4, position);
                break;
        }
    }




    private void configureViewHolder2(ViewHolder2 vh2, int position) {
        vh2.getImageView().setImageResource(R.drawable.fbmelogo1);
       vh2.getImageView().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Uri uri = Uri.parse("fb-messenger://user/");
               uri = ContentUris.withAppendedId(uri,580366185473843L);
               Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
               v.getContext().startActivity(intent2);
           }
       });


    }

    private void configureViewHolder3(ViewHolder3 vh3, int position) {
        Album album = (Album) items.get(position);
        if (album != null) {


            

        }




    }



}