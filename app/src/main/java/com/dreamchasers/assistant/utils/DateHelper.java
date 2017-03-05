package com.dreamchasers.assistant.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ka on 2/12/2017.
 */

public class DateHelper {

    public Date string2Date(String strDate){
        Log.v("dateTry", strDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = null;
        try {
            date = format.parse(strDate);

            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.v("Date", "Cant convert string to date");
        }
        return date;
    }

    public String stringTime(int hours, int minutes){
        String hString = String.valueOf(hours), mString = String.valueOf(minutes);
        if(hString.length() <= 1) hString = "0"+hString;
        if(mString.length() <= 1) mString = "0"+mString;

        return hString+":"+mString;
    }

}
