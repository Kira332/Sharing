package com.result;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private Object data;

    Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;

    }



}
