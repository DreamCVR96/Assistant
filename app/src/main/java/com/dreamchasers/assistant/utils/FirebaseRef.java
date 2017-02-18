package com.dreamchasers.assistant.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by macpro on 2/16/17.
 */

public class FirebaseRef {
    public static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);

        }
        return mDatabase;
    }

}