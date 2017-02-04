package com.dreamchasers.assistant.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.dreamchasers.assistant.activities.MainActivity;
import com.dreamchasers.assistant.fragments.AlarmsFragment;
import com.dreamchasers.assistant.fragments.NewsFeedFragment;
import com.dreamchasers.assistant.fragments.TabFragment;
import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.models.MainView;
import com.dreamchasers.assistant.models.Reminder;

import static android.R.attr.fragment;
import static android.util.Log.v;

public class ViewPageAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.CustomTabProvider {

    private final int[] ICONS = {



            R.drawable.ic_home_white_24dp,
            R.drawable.selector_icon_active,
            R.drawable.ic_alarm_on_white_24dp,
            R.drawable.selector_icon_inactive



    };



    public ViewPageAdapter(FragmentManager fm) {
        super(fm);


    }

    @Override
    public void tabSelected(View view) {
        view.setSelected(true);

    }

    @Override
    public void tabUnselected(View view) {
        view.setSelected(false);

    }


    @Override
    public View getCustomTabView(ViewGroup parent, int position) {
        FrameLayout customLayout = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_tab, parent, false);
        ((ImageView) customLayout.findViewById(R.id.image)).setImageResource(ICONS[position]);

        return customLayout;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return ICONS.length;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:

            bundle.putInt("TYPE", MainView.HOME);
            Fragment newsF1 = new NewsFeedFragment();
            newsF1.setArguments(bundle);

            return newsF1;

            case 1:
                bundle.putInt("TYPE", Reminder.ACTIVE);
                Fragment tab1 = new TabFragment();
                tab1.setArguments(bundle);
                return tab1;



            case 2:

                bundle.putInt("TYPE", Reminder.INACTIVE);
                Fragment tab2 = new TabFragment();
                tab2.setArguments(bundle);
                return tab2;

            case 3:

                //  bundle.putInt("TYPE", MainView.HOME);
                Fragment alarmF = new AlarmsFragment();
                // alarmF.setArguments(bundle);

                return alarmF;
            default:
                Log.v("Fragment getItem", "Default, null");
                return null;

        }
    }
}