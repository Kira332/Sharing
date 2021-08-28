package com.result;

public enum ResultCode {
    SUCCESS(200),
    FAIL(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}