package com.example.callcenter.intarface;

public interface ISignalRListener {
    void ReceiveCallPhone(String phone);
    void ReceiveMessage(String message);
}
