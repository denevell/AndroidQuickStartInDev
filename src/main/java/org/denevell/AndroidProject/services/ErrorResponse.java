package org.denevell.AndroidProject.services;

public class ErrorResponse {
    public int responseCode;
    public String responseMessage;
    public String url;

    public void fill(int httpCode, String errorMessage, String url) {
       this.responseCode = httpCode;
       this.responseMessage = errorMessage;
       this.url = url;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
