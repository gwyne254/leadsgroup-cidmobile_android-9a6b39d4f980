package com.bauart.calleridreputation.json.notificationSettings;

import com.google.gson.annotations.SerializedName;

public class MetaData {
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }
}
