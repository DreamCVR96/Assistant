package com.dreamchasers.assistant.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by macpro on 2/16/17.
 */

public class FirebaseRef {
    public static DatabaseReference mDatabase;

    public static DatabaseReference getDatabase() {
        if (mDatabase == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance().getReference();

        }
        return mDatabase;
    }

}