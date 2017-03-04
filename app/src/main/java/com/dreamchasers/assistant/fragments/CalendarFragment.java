package com.dreamchasers.assistant.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.activities.DrawableCalendarEvent;
import com.dreamchasers.assistant.activities.DrawableEventRenderer;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarManager;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.github.tibolte.agendacalendarview.models.WeekItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by macpro on 2/21/17.
 */

public class CalendarFragment extends Fragment implements CalendarPickerController{

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @BindView(R.id.agenda_calendar_view)
    AgendaCalendarView mAgendaCalendarView;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);


        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -1);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        List<CalendarEvent> eventList = new ArrayList<>();
        mockList(eventList);

          //  mAgendaCalendarView.setNestedScrollingEnabled(true);
        try {
            mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
            mAgendaCalendarView.addEventRenderer(new DrawableEventRenderer());

        } catch (Exception e){}


      //  mScrollView.bringChildToFront(mAgendaCalendarView);
      //  MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView);


        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView);
    }



    private void mockList(List<CalendarEvent> eventList) {
        Calendar startTime1 = Calendar.getInstance();
        Calendar endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.MONTH, 1);
        BaseCalendarEvent event1 = new BaseCalendarEvent("Thibault travels in Iceland", "A wonderful journey!", "Iceland",
                ContextCompat.getColor(getActivity(), R.color.primary), startTime1, endTime1, true);
        eventList.add(event1);

        Calendar startTime2 = Calendar.getInstance();
        startTime2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar endTime2 = Calendar.getInstance();
        endTime2.add(Calendar.DAY_OF_YEAR, 3);
        BaseCalendarEvent event2 = new BaseCalendarEvent("Visit to Dalvík", "A beautiful small town", "Dalvík",
                ContextCompat.getColor(getActivity(), R.color.primary), startTime2, endTime2, true);
        eventList.add(event2);

    }
    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d("22", String.format("Selected day: %s", dayItem));

    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Log.d("22", String.format("Selected event: %s", event));

    }

    @Override
    public void onScrollToDate(Calendar calendar) {

    }

}
