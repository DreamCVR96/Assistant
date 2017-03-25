package com.dreamchasers.assistant.activities;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.dreamchasers.assistant.R;
import com.dreamchasers.assistant.database.DatabaseHelper;
import com.dreamchasers.assistant.models.Reminder;
import com.dreamchasers.assistant.receivers.AlarmReceiver;
import com.dreamchasers.assistant.utils.AlarmUtil;
import com.dreamchasers.assistant.utils.DateHelper;
import com.dreamchasers.assistant.utils.FirebaseRef;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by macpro on 3/23/17.
 */

public class OnChildAdded extends Service {


    public final String DEFAULTUSERID = "N/A";


    public void onCreate(){
        super.onCreate();
        Log.w("OnChildAddedService", "OnChildAddedService---OnCreate ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Adding a childevent listener to firebase

        SharedPreferences sharedPreferences = getSharedPreferences("userKey", MODE_PRIVATE);
        String usrKey2 = sharedPreferences.getString("userKey", DEFAULTUSERID);

        FirebaseRef.getDatabase().child("server/saving-data/sidekicks/users")
                .child(usrKey2).child("reminders")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        if(!dataSnapshot.child("by_android").exists()) {


                            String reminderId = dataSnapshot.getKey();
                            if (dataSnapshot.child("reminder_text").exists())
                                Log.e("deivui", (String) dataSnapshot.child("reminder_text").getValue());

                            String reminder = String.valueOf(dataSnapshot.getValue());

                            Date convertedDate = null;
                            String date = String.valueOf(dataSnapshot.child("datetime").getValue());
                            convertedDate = new DateHelper().string2Date(date);
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                            String time1 = sdf.format(convertedDate);


                            if (dataSnapshot.child("reminder_text").exists()) {
                                DatabaseHelper database = DatabaseHelper.getInstance(getBaseContext());
                                int id1 = database.getLastNotificationId() + 1;
                                String dummyIcon = getString(R.string.default_icon_value);
                                Reminder reminderNew = new Reminder()
                                        .setId(id1)
                                        .setTitle("Don't forget to " + String.valueOf(dataSnapshot.child("reminder_text").getValue()))
                                        .setIcon(dummyIcon)
                                        .setContent(time1);

                                database.addNotification(reminderNew);

                                database.close();
                                Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                                Date setDate = new DateHelper().string2Date(String.valueOf(dataSnapshot.child("datetime").getValue()));

                                Calendar calendarDummy = (Calendar) Calendar.getInstance().clone();

                                if (setDate == null) {
                                    Log.v("asd", "asdasdsadas");
                                } else {
                                    Log.v("ciacia", setDate.toString());
                                    calendarDummy.setTime(setDate);
                                    // AlarmUtil.setAlarm(getBaseContext(), alarmIntent, reminderNew.getId(), calendarDummy);
                                    AlarmUtil.setAlarm(getApplicationContext(), alarmIntent, reminderNew.getId(), calendarDummy);
                                }
                            }
                        }


    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
});

        return START_STICKY;
        }

        }