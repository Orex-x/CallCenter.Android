package com.example.callcenter.modes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.callcenter.MainActivity;
import com.example.callcenter.intarface.ISignalRListener;
import com.example.callcenter.services.CallBook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Single;
import okhttp3.internal.http2.Header;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerController {
    private static String _host = "https://fcce-62-217-190-128.ngrok.io";
    private static String _token;
    private static HubConnection _hubConnection;
    private Retrofit _retrofit;
    private static Context _context;
    private static Activity _activity;

    private static final int REQUEST_CODE_READ_CALL_LOG = 2;
    private static boolean READ_FILES_GRANTED = false;

    private static ISignalRListener iSignalRListener;
    public void setISignalRListener(ISignalRListener listener) {
        iSignalRListener = listener;
    }

    public static String get_token() {
        return _token;
    }

    public static void set_token(String _token) {
        ServerController._token = _token;
    }

    public void disconnect(){
        if(_hubConnection != null){
            _hubConnection.stop();
        }
    }

    public void startSignalRConnection(Context context, Activity activity) {
        if(_token != null){

            String str = android.os.Build.MODEL;

            HashMap<String, String> HEADERS = new HashMap<>();
            HEADERS.put("Hostname", str);

            _hubConnection = HubConnectionBuilder.create(_host + "/ChatHub")
                    .withHeaders(HEADERS)
                    .withAccessTokenProvider(Single.defer(() -> {
                        return Single.just(_token);
                    })).build();


            _hubConnection.on("ReceiveCallPhone", (phone) -> {
                Log.d("Call receiveCallPhone", phone);
                if(iSignalRListener != null){
                    iSignalRListener.ReceiveCallPhone(phone);
                }
            }, String.class);

            _hubConnection.on("ReceiveTerminateConnection", () -> {
                if(iSignalRListener != null){
                    iSignalRListener.LogOut();
                }
            });



            new HubConnectionTask().execute(_hubConnection);
            _context = context;
            _activity = activity;
        }
    }

    public static void sendLastCall(){
        setPermissionReadCallLogs();
    }


    public static void setPermissionReadCallLogs(){
        // получаем разрешения
        int hasPermissionReadCallLogs =
                ContextCompat.checkSelfPermission(_context, Manifest.permission.READ_CALL_LOG);
        // если устройство до API 23, устанавливаем разрешение
        if (hasPermissionReadCallLogs == PackageManager.PERMISSION_GRANTED) {
            READ_FILES_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(_activity,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    REQUEST_CODE_READ_CALL_LOG);
        }
        // если разрешение установлено
        if (READ_FILES_GRANTED) {
            new myThread().start();
        }
    }

    static class myThread extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Call call = CallBook.getLastCall(_context);
            _hubConnection.send("CallLog", call);
        }
    }

    private void startRx() {
        _retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(_host + "/api/")
                .build();
    }

    public Retrofit get_retrofit() {
        if(_retrofit == null)
            startRx();
        return _retrofit;
    }

    class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            HubConnection hubConnection = hubConnections[0];
            //Start the connection and wait for it to succeed
            hubConnection.start().blockingAwait();
            Log.d("getConnectionState ",hubConnection.getConnectionState().toString());
            return null;
        }
    }
}
