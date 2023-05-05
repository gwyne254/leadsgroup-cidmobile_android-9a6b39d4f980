package com.bauart.calleridreputation.json.notificationSettings;

import com.google.gson.annotations.SerializedName;

public class SettingNotification {
    @SerializedName("Email")
    private String email;

    @SerializedName("MobileAppPush")
    private String push;

    @SerializedName("name")
    private String name;

    @SerializedName("key")
    private String key;

    public String getEmail() {
        return email;
    }

    public String getPush() {
        return push;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
