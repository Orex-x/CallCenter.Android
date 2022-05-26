package com.example.callcenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.callcenter.fragment.FragmentAuth;
import com.example.callcenter.fragment.FragmentRegistration;
import com.example.callcenter.intarface.APIService;
import com.example.callcenter.intarface.OnFragmentAuthListener;
import com.example.callcenter.intarface.OnFragmentRegistrationListener;
import com.example.callcenter.modes.RegistrationModel;
import com.example.callcenter.modes.ServerController;
import com.example.callcenter.modes.TokenResponse;
import com.example.callcenter.modes.User;

import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AuthActivity extends AppCompatActivity implements OnFragmentAuthListener,
        OnFragmentRegistrationListener {


    private FragmentAuth fragmentAuth;
    private FragmentRegistration fragmentRegistration;

    private final String SIMPLE_FRAGMENT_AUTH_TAG = "SIMPLE_FRAGMENT_AUTH_TAG";
    private final String SIMPLE_FRAGMENT_REGISTRATION_TAG = "SIMPLE_FRAGMENT_REGISTRATION_TAG";
    private String current_fragment = SIMPLE_FRAGMENT_AUTH_TAG;

    APIService api;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("current_fragment", current_fragment);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ServerController controller = new ServerController();
        api = controller.get_retrofit().create(APIService.class);

        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            fragmentAuth = (FragmentAuth)
                    getSupportFragmentManager().findFragmentByTag(SIMPLE_FRAGMENT_AUTH_TAG);
            fragmentRegistration = (FragmentRegistration)
                    getSupportFragmentManager().findFragmentByTag(SIMPLE_FRAGMENT_REGISTRATION_TAG);

            current_fragment = savedInstanceState.getString("current_fragment");

        }
        // only create fragment if they haven't been instantiated already
        if (fragmentAuth == null)
            fragmentAuth = new FragmentAuth();
        if(fragmentRegistration == null)
            fragmentRegistration = new FragmentRegistration();

        fragmentAuth.setOnFragmentAuthListener(this);
        fragmentRegistration.setOnFragmentRegistrationListener(this);


        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(current_fragment.equals(SIMPLE_FRAGMENT_AUTH_TAG)){
            fragmentTransaction.replace(R.id.fragment, fragmentAuth,
                    SIMPLE_FRAGMENT_AUTH_TAG);
        }else if(current_fragment.equals(SIMPLE_FRAGMENT_REGISTRATION_TAG)){
            fragmentTransaction.replace(R.id.fragment, fragmentRegistration,
                    SIMPLE_FRAGMENT_REGISTRATION_TAG);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void clickOpenRegistrationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragmentRegistration, SIMPLE_FRAGMENT_REGISTRATION_TAG);
        fragmentTransaction.commit();
        current_fragment = SIMPLE_FRAGMENT_REGISTRATION_TAG;
    }



    @Override
    public void clickOpenAuthFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragmentAuth, SIMPLE_FRAGMENT_AUTH_TAG);
        fragmentTransaction.commit();
        current_fragment = SIMPLE_FRAGMENT_AUTH_TAG;
    }

    @Override
    public void clickSignIn(String  login, String password) {
        Log.d("clickSignIn", "start");

        api.Token(login, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<TokenResponse>() {
                    @Override
                    public void call(TokenResponse tokenResponse) {

                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<TokenResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d("clickSignIn", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("clickSignIn", e.getMessage());
                    }

                    @Override
                    public void onNext(TokenResponse tokenResponse) {
                        Log.d("clickSignIn", tokenResponse.getAccess_token());
                        ServerController.set_token(tokenResponse.getAccess_token());
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void clickRegistration(RegistrationModel model) {
        Log.d("clickRegistration", "start");
        api.Registration(model)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<TokenResponse>() {
                    @Override
                    public void call(TokenResponse tokenResponse) {

                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<TokenResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d("clickRegistration", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("clickRegistration", e.getMessage());
                        Toast.makeText(AuthActivity.this,
                                "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(TokenResponse tokenResponse) {
                        Log.d("clickRegistration", tokenResponse.getAccess_token());
                        ServerController.set_token(tokenResponse.getAccess_token());
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}