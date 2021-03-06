package com.result;
public class ResultFactory {

    public static Result buildSuccessResult(String message,Object data) {
        return buildResult(ResultCode.SUCCESS, message, data);
    }
    public static Result buildSuccessResult() {
        return buildResult(ResultCode.SUCCESS, null, null);
    }
    public static Result buildFailResult(String message) {
        return buildResult(ResultCode.FAIL, message, null);
    }
    public static Result buildResult(ResultCode resultCode, String message, Object data) {
        return buildResult(resultCode.code, message, data);
    }
    public static Result buildResult(int resultCode, String message, Object data) {
        return new Result(resultCode, message, data);
    }
}
