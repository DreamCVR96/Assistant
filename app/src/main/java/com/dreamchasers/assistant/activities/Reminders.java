package com.dreamchasers.assistant.activities;

/**
 * Created by macpro on 2/16/17.
 */

public  class Reminders {


    private String datetime;
    private String reminder_text;


    public Reminders() {
        // Needed for Firebase
    }

    public Reminders(String datetime, String reminder_text) {
        this.datetime = datetime;
       this.reminder_text = reminder_text;
    }


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getReminder_text() {
        return reminder_text;
    }

    public void setReminder_text(String reminder_text) {
        this.reminder_text = reminder_text;
    }

}