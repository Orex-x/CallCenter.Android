package com.example.callcenter.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.callcenter.R;
import com.example.callcenter.intarface.OnFragmentAuthListener;


public class FragmentAuth extends Fragment {
    Button btnRegistration, btnSignIn;
    EditText edtLogin, edtPassword;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_auth, container, false);
        btnRegistration = v.findViewById(R.id.btnRegistration);
        btnSignIn = v.findViewById(R.id.btnSignIn);
        edtLogin = v.findViewById(R.id.edtLogin);
        edtPassword = v.findViewById(R.id.edtPassword);

        btnSignIn = v.findViewById(R.id.btnSignIn);

        btnRegistration.setOnClickListener(view -> {
            if(mListener != null)
                mListener.clickOpenRegistrationFragment();
        });

        btnSignIn.setOnClickListener(view -> {
            if(mListener != null){
                mListener.clickSignIn(
                        edtLogin.getText().toString(),
                        edtPassword.getText().toString()
                );
            }
        });

        return v;
    }
    private static OnFragmentAuthListener mListener;

    public void setOnFragmentAuthListener(OnFragmentAuthListener listener) {
        mListener = listener;
    }
}