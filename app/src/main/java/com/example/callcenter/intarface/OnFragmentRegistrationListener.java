package com.example.callcenter.intarface;


import com.example.callcenter.modes.RegistrationModel;
import com.example.callcenter.modes.User;

public interface OnFragmentRegistrationListener {
    void clickOpenAuthFragment();
    void clickRegistration(RegistrationModel model);
}
