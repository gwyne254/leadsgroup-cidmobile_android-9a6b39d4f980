package com.bauart.calleridreputation.json.notifications;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationPage {
    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("data")
    private ArrayList<NotificationData> notifications;

    public int getCurrentPage() {
        return currentPage;
    }

    public ArrayList<NotificationData> getNotifications() {
        return notifications;
    }
}
