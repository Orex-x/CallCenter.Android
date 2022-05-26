package com.example.callcenter.intarface;

import com.example.callcenter.modes.RegistrationModel;
import com.example.callcenter.modes.TokenResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface APIService {
    @POST("account/Registration")
    Observable<TokenResponse> Registration(@Body RegistrationModel model);

    @POST("account/token")
    Observable<TokenResponse> Token(@Query("login") String login, @Query("password") String password);
}
