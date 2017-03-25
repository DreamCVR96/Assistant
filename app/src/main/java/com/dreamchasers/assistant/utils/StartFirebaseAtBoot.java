package com.dreamchasers.assistant.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dreamchasers.assistant.activities.OnChildAdded;

/**
 * Created by macpro on 3/23/17.
 */

public class StartFirebaseAtBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, OnChildAdded.class));
    }
}