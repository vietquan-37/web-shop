package com.vietquan.security.exception;

public class ResetTokenExpired extends Exception {
    public  ResetTokenExpired(String message) {
        super(message);
    }
}
