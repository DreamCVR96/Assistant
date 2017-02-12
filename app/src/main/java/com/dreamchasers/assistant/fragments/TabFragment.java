package com.dreamchasers.assistant.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamchasers.assistant.activities.MainActivity;
import com.dreamchasers.assistant.database.DatabaseHelper;
import com.dreamchasers.assistant.models.Reminder;
import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.adapters.ReminderAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TabFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @BindView(R.id.empty_text) TextView emptyText;
    @BindView(R.id.empty_view) LinearLayout linearLayout;
    @BindView(R.id.empty_icon) ImageView imageView;

    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminderList;
    private int argsType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);


        argsType = this.getArguments().getInt("TYPE");
        if (argsType == Reminder.INACTIVE) {
            emptyText.setText(R.string.no_inactive);
            imageView.setImageResource(R.drawable.ic_notifications_off_black_empty);
            Log.v("FRAGMENT GET ITEM", "FUCK");
//

        }


        argsType = this.getArguments().getInt("TYPE");
        if (argsType == Reminder.ACTIVE) {
            emptyText.setText(R.string.no_active);
            imageView.setImageResource(R.drawable.ic_notifications_black_empty);
            Log.v("FRAGMENT GET ITEM", "wasssssuuup");


        }



        reminderList = getListData();
        reminderAdapter = new ReminderAdapter(getContext(), reminderList);
        recyclerView.setAdapter(reminderAdapter);


        if (reminderAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);

            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public List<Reminder> getListData() {
        DatabaseHelper database = DatabaseHelper.getInstance(getContext().getApplicationContext());
        List<Reminder> reminderList = database.getNotificationList(argsType);
        database.close();
        return reminderList;
    }

    public void updateList() {
        reminderList.clear();
        reminderList.addAll(getListData());
        reminderAdapter.notifyDataSetChanged();
        Log.v("RemindertypeNum", "" + argsType);

        if (reminderAdapter.getItemCount() == 0 ) {
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }


    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateList();
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, new IntentFilter("BROADCAST_REFRESH"));
        updateList();
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);
        super.onPause();
    }
}