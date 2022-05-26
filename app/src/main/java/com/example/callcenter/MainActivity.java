package com.example.callcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.callcenter.intarface.APIService;
import com.example.callcenter.intarface.ISignalRListener;
import com.example.callcenter.modes.ServerController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.util.logging.Logger;

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
        serverController.startSignalRConnection();
    }


    void call(String phone){
        Intent myIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
    }


     void setPermission(String phone){
        // hubConnection.send("SendMessage", "asd", "asd");
        // получаем разрешения
        int hasReadFilesPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        // если устройство до API 23, устанавливаем разрешение
        if (hasReadFilesPermission == PackageManager.PERMISSION_GRANTED) {
            CALL_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
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