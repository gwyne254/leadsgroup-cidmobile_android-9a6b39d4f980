package com.bauart.calleridreputation.json.notifications;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationResponse {
    @SerializedName("items")
    private NotificationPage items;

    public NotificationPage getItems() {
        return items;
    }
}
