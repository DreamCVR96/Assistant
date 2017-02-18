package com.dreamchasers.assistant.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dreamchasers.assistant.R;


/**
 * Created by macpro on 2/16/17.
 */

public  class RemindersHolder extends RecyclerView.ViewHolder {
    private final TextView mReminderText;
    private final TextView mReminderDatetime;

    public RemindersHolder(View itemView) {
        super(itemView);
        mReminderText = (TextView) itemView.findViewById(R.id.reminderText);
        mReminderDatetime = (TextView) itemView.findViewById(R.id.reminderDatetime);
    }

    public void setmReminderText(String reminder_text) {
        mReminderText.setText(reminder_text);
    }

    public void setmReminderDatetime(String datetime) {
        mReminderDatetime.setText(datetime);
    }
}