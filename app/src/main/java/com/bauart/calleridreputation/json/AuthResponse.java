package com.bauart.calleridreputation.json;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getMessage() {
        return message;
    }
}
