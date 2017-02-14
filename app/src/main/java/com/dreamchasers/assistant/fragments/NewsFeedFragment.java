package com.dreamchasers.assistant.fragments;



import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.adapters.AlbumAdapter;
import com.dreamchasers.assistant.models.Album;
import com.dreamchasers.assistant.models.MainView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by macpro on 11/28/16.
 */

public class NewsFeedFragment extends Fragment {


    private int argsType;
    private String mVoiceInput = "";
    private AlbumAdapter adapter;
    private List<Album> albumList;


    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
    ScaleAnimation shrinkAnim;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private static final String userId = "dUDWCUkPXAI:APA91bF8TlQlEZdXLVQMJQktY6J2" +
            "bcOPZRHvbMr41Jx3Lz1hQxo6KNeRPBEGtoarY5DiMCH2C8XAy2fdcVrO5XXbbOu_XtY1KqM" +
            "WiRhowm6UKF_4v5v83e4RsGmg_fnEcR8p38q-s1uJ";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newsfeed, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);

        initCollapsingToolbar();






        argsType = this.getArguments().getInt("TYPE");
        if (argsType == MainView.HOME) {

        //    imageView.setImageResource(R.drawable.ic_notifications_black_empty);
            Log.v("FRAGMENT GET ITEM", "MAIN/O");


          //  linearLayout.setVisibility(View.GONE);
           // relativeLayout.setVisibility(View.VISIBLE);

        }


    }










    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getView().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) getView().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    appBarLayout.setVisibility(View.INVISIBLE);
                    isShow = true;
                }
                else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    appBarLayout.setVisibility(View.VISIBLE);

                    isShow = false;
                }
            }
        });
    }



    }

