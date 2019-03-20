package com.github.kevin.library.bean;

public class ResponseBean {
    private String error_code;
    private String reason;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "error_code='" + error_code + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

}
