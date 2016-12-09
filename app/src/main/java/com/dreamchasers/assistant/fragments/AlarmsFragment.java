package com.dreamchasers.assistant.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.activities.MainActivity;
import com.dreamchasers.assistant.models.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macpro on 11/28/16.
 */

public class AlarmsFragment extends Fragment {

    @BindView(R.id.recycler_view_alarms) RecyclerView recyclerViewMain;

    @BindView(R.id.empty_view5) LinearLayout linearLayout;
    @BindView(R.id.view_alarm) RelativeLayout relativeLayout;
    @BindView(R.id.empty_icon5) ImageView imageView;
    @BindView(R.id.sendText2) EditText sendText;
    private int argsType;
    private String mVoiceInput = "";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarms, container, false);




    }
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager1= new LinearLayoutManager(getContext());
        recyclerViewMain.setLayoutManager(layoutManager1);


        sendText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String sndText = sendText.getText().toString();
                    try {
                        ((MainActivity)getActivity()).run1(sndText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    return true;
                }
                return false;
            }
        });

     //   argsType = this.getArguments().getInt("TYPE");
       // if (argsType == MainView.HOME) {
         //   emptyText.setText("GOT IT BOYS, 2");
            imageView.setImageResource(R.drawable.ic_notifications_black_empty);
            Log.v("FRAGMENT GET ITEM", "2");
            sendText.setVisibility(View.VISIBLE);
            recyclerViewMain.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
      //      ((MainActivity)getActivity()).animateFab(2);
   //     }

       Log.v("Context", "view.getContext()" + view.getContext());
       //   ((MainActivity)getActivity()).hideFab();
    }


}
