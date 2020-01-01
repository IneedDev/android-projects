package com.example.app_enum;

public enum States {
    TN("TamilNadu"),
    AP("AndhraPradesh"),
    KA("Karnataka"),
    KE("Kerala"),
    MP("MathyaPradesh"),
    UP("UttarPradesh");

    private String strState;

    States(String aState) {
        strState = aState;
    }

    @Override public String toString() {
        return strState;
    }
}