package com.bauart.calleridreputation.json.notificationSettings;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SettingsNotifications {
    @SerializedName("notify")
    private ArrayList<SettingNotification> setting;

    public ArrayList<SettingNotification> getSetting() {
        return setting;
    }
}
