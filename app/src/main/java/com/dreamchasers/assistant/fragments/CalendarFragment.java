package com.dreamchasers.assistant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamchasers.assistant.R;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macpro on 2/21/17.
 */

public class CalendarFragment extends Fragment {

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @BindView(R.id.agenda_calendar_view)
    AgendaCalendarView agendaCalendarView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);













    }

}
