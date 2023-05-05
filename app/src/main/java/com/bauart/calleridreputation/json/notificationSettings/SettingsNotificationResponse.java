package com.bauart.calleridreputation.json.notificationSettings;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SettingsNotificationResponse {
    @SerializedName("data")
    private SettingsNotifications settings;

    @SerializedName("meta")
    private ArrayList<MetaData> metaData;

    public SettingsNotifications getSettings() {
        return settings;
    }

    public ArrayList<MetaData> getMetaData() {
        return metaData;
    }
}
