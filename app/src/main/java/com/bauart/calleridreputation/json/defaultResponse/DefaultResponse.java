package com.bauart.calleridreputation.json.defaultResponse;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse {
    @SerializedName("message")
    private DefaultMessage message;

    @SerializedName("response")
    private DefaultStatus response;

    public DefaultMessage getMessage() {
        return message;
    }

    public DefaultStatus getResponse() {
        return response;
    }
}
