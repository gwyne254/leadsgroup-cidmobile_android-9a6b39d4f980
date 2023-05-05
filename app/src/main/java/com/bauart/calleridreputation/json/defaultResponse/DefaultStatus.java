package com.bauart.calleridreputation.json.defaultResponse;

import com.google.gson.annotations.SerializedName;

public class DefaultStatus {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }
}
