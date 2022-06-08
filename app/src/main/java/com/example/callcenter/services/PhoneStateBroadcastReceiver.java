package com.example.callcenter.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.callcenter.modes.ServerController;

public class PhoneStateBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneStateBroadcast";
    String incoming_number;
    private static int sendCallLog = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            //получаем исходящий номер
            sendCallLog++;
            incoming_number = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            Log.d(TAG, "phoneNr: "+incoming_number);
        } else if (intent.getAction().equals("android.intent.action.PHONE_STATE")){

            String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);


            Log.d(TAG, "phone_state: " + phone_state);

            if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                //телефон звонит, получаем входящий номер
                incoming_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                //телефон находится в режиме звонка (набор номера / разговор)
            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Log.d(TAG, "EXTRA_STATE_IDLE");
                if(sendCallLog == 1){
                    ServerController.sendLastCall();
                    sendCallLog = -1;
                    return;
                }
                sendCallLog++;

            }
        }
    }
}
