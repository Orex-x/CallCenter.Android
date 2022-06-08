package com.example.callcenter.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;

import androidx.annotation.RequiresApi;

import com.example.callcenter.modes.Call;

import java.util.ArrayList;
import java.util.Calendar;

public class CallBook {


    @SuppressLint("Range")
    public static ArrayList<Call> getAllCalls(Context context){
        String[] projection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
        };
        String where = "";

        Cursor cursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                where,
                null,
                null
        );

        ArrayList<Call> calls = new ArrayList<>();
        if (cursor.moveToNext()) {
            do {
                long _id = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID));
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(date);

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinutes = calendar.get(Calendar.MINUTE);
                String dateStr = mMonth + "/" + mDay + " " + mHour + ":" + mMinutes;

                calls.add(new Call(number, name, dateStr));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if(calls.stream().count() > 10) break;
                }
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }

        return calls;
    }

    @SuppressLint("Range")
    public static Call getLastCall(Context context){
        String[] projection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
        };
        String where = "";

        Cursor cursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                where,
                null,
                null
        );

        Call call = new Call();

        cursor.moveToLast();
        int count = cursor.getCount();
        if (cursor.moveToLast()) {
            long _id = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);

            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            int mHour = calendar.get(Calendar.HOUR_OF_DAY);
            int mMinutes = calendar.get(Calendar.MINUTE);
            String dateStr = mMonth + "/" + mDay + " " + mHour + ":" + mMinutes;

            if(name == null){
                name = "Unknown";
            }

            if(name.length() == 0){
                name = "Unknown";
            }

            call = new Call(number, name, dateStr);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }

        return call;
    }
}
