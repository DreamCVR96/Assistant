package com.dreamchasers.assistant.fragments;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.activities.Reminders;
import com.dreamchasers.assistant.models.Album;
import com.dreamchasers.assistant.models.MainView;
import com.dreamchasers.assistant.utils.FirebaseRef;
import com.dreamchasers.assistant.viewholders.RemindersHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.dreamchasers.assistant.utils.FirebaseRef.getDatabase;


/**
 * Created by macpro on 11/28/16.
 */

public class NewsFeedFragment extends Fragment {


    private int argsType;
    private String mVoiceInput = "";
    private List<Album> albumList;
    public final String DEFAULTUSERID = "N/A";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;
    ScaleAnimation shrinkAnim;



    //Getting reference to Firebase Database
    private DatabaseReference remindersRef = FirebaseRef.getDatabase()
            .child("server/saving-data/sidekicks/users");


    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newsfeed, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);



        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userKey", MODE_PRIVATE);
        String usrKey2 = sharedPreferences.getString("userKey", DEFAULTUSERID);




        if(usrKey2.equals(DEFAULTUSERID)){
            remindersRef = remindersRef.child(FirebaseInstanceId.getInstance().getToken())
                    .child("reminders");



        } else {

            remindersRef = remindersRef.child(usrKey2)
                    .child("reminders");

        }


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        mAdapter = new FirebaseRecyclerAdapter<Reminders, RemindersHolder>(Reminders.class, R.layout.reminders_card, RemindersHolder.class, remindersRef) {
            @Override
            public void populateViewHolder(RemindersHolder remindersViewHolder, Reminders remindersData, int position) {
                remindersViewHolder.setmReminderDatetime(remindersData.getDatetime());
                remindersViewHolder.setmReminderText(remindersData.getReminder_text());

            }
        };
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(mAdapter);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);


       // mRecyclerView.setVisibility(View.VISIBLE);


/*        argsType = this.getArguments().getInt("TYPE");
        if (argsType == MainView.HOME) {

        //    imageView.setImageResource(R.drawable.ic_notifications_black_empty);
            Log.v("FRAGMENT GET ITEM", "MAIN/O");


          //  linearLayout.setVisibility(View.GONE);
           // relativeLayout.setVisibility(View.VISIBLE);

        }*/


    }




    }

