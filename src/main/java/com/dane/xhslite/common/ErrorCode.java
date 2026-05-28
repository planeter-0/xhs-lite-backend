package com.dane.xhslite.common;

public enum ErrorCode {
    BAD_REQUEST(40000, "Bad request"),
    NOT_FOUND(40400, "Resource not found"),
    CONFLICT(40900, "Resource conflict"),
    INTERNAL_ERROR(50000, "Internal server error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
