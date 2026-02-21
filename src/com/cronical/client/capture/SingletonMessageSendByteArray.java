package com.cronical.client.capture;

public class SingletonMessageSendByteArray {
    private static SingletonMessageSendByteArray instance;

    private SingletonMessageSendByteArray() {}

    public static SingletonMessageSendByteArray getInstance() {
        if (instance == null) {
            instance = new SingletonMessageSendByteArray();
        }
        return instance;
    }
}