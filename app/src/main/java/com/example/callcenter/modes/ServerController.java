package com.example.callcenter.modes;

import android.os.AsyncTask;
import android.util.Log;

import com.example.callcenter.intarface.ISignalRListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerController {
    private static String _host = "https://233d-62-217-190-128.ngrok.io";
    private static String _token;
    private static HubConnection _hubConnection;
    private Retrofit _retrofit;

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

    public void startSignalRConnection() {
        if(_token != null){
            _hubConnection = HubConnectionBuilder.create(_host + "/ChatHub")
                    .withAccessTokenProvider(Single.defer(() -> {
                        return Single.just(_token);
                    })).build();

   /*         _hubConnection.on("ReceiveMeMessage", (message) -> {
                Log.d("Call ReceiveMeMessage", message);
                if(iSignalRListener != null){
                    iSignalRListener.ReceiveMessage(message);
                }
            }, String.class);*/

            _hubConnection.on("ReceiveCallPhone", (phone) -> {
                Log.d("Call receiveCallPhone", phone);
                if(iSignalRListener != null){
                    iSignalRListener.ReceiveCallPhone(phone);
                }
            }, String.class);

            new HubConnectionTask().execute(_hubConnection);
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
