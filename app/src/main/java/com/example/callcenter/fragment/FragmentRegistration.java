package com.example.callcenter.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.callcenter.R;
import com.example.callcenter.intarface.OnFragmentRegistrationListener;
import com.example.callcenter.modes.RegistrationModel;
import com.example.callcenter.modes.User;


public class FragmentRegistration extends Fragment {

    Button btnBack, btnRegistration;
    EditText edtName, edtLogin, edtPasswordR, edtPassword;
    CheckBox checkBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        btnBack = v.findViewById(R.id.btnBack);
        btnRegistration = v.findViewById(R.id.btnRegistration);
        edtName = v.findViewById(R.id.edtName);
        edtLogin = v.findViewById(R.id.edtLogin);
        edtPassword = v.findViewById(R.id.edtPassword);

        btnBack.setOnClickListener(view -> {
            if(mListener != null)
                mListener.clickOpenAuthFragment();
        });

        btnRegistration.setOnClickListener(view -> {
            RegistrationModel model = new RegistrationModel();
            model.setName(edtName.getText().toString());
            model.setLogin(edtLogin.getText().toString());
            model.setPassword(edtPassword.getText().toString());

            if(mListener != null){
                mListener.clickRegistration(model);
            }
        });

        return v;
    }

    private static OnFragmentRegistrationListener mListener;

    public void setOnFragmentRegistrationListener(OnFragmentRegistrationListener listener) {
        mListener = listener;
    }
}