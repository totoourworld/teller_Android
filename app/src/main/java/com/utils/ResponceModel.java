package com.utils;

public class ResponceModel {

    private String status;
    private int code;
    private String message;
    private String response;
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setResponse(String  response) {
        this.response = response;
    }
    public String getResponse() {
        return response;
    }

}


