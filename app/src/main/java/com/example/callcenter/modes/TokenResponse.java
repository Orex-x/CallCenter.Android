package com.example.callcenter.modes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("access_token")
    @Expose
    public String access_token;

    @SerializedName("username")
    @Expose
    public String username;

    public TokenResponse() {
    }

    public TokenResponse(String access_token, String username) {
        this.access_token = access_token;
        this.username = username;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
