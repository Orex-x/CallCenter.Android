package com.example.callcenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.callcenter.intarface.ISignalRListener;
import com.example.callcenter.modes.ServerController;
import com.example.callcenter.services.CallBook;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ISignalRListener {

    private static final int REQUEST_CODE_CALL = 1;
    private static boolean CALL_GRANTED = false;



    Button btn;
    ServerController serverController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        serverController = new ServerController();
        serverController.setISignalRListener(this);
        serverController.startSignalRConnection(this, this);


    }




    void call(String phone){
        Intent myIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
    }


     void setPermission(String phone){

        // получаем разрешения
        int hasCALL_PHONEPermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE);

         int hasREAD_PHONE_STATEPermission = ContextCompat.checkSelfPermission(
                 this, Manifest.permission.READ_PHONE_STATE);

         int hasPROCESS_OUTGOING_CALLSPermission = ContextCompat.checkSelfPermission(
                 this, Manifest.permission.PROCESS_OUTGOING_CALLS);


        // если устройство до API 23, устанавливаем разрешение
        if (hasCALL_PHONEPermission == PackageManager.PERMISSION_GRANTED
                && hasREAD_PHONE_STATEPermission == PackageManager.PERMISSION_GRANTED
                && hasPROCESS_OUTGOING_CALLSPermission == PackageManager.PERMISSION_GRANTED) {
            CALL_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.PROCESS_OUTGOING_CALLS},
                    REQUEST_CODE_CALL);
        }
        // если разрешение установлено
        if (CALL_GRANTED) {
            call(phone);
        }
    }

    @Override
    public void ReceiveCallPhone(String phone) {
        setPermission(phone);

    }

    @Override
    public void ReceiveMessage(String message) {
        Log.d("ReceiveMessage",  message);
    }
}