package com.bauart.calleridreputation.json.notifications;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Notification implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("phone_id")
    private int phoneId;

    @SerializedName("title")
    private String title;

    @SerializedName("number")
    private String number;

    @SerializedName("service")
    private String service;

    @SerializedName("icon")
    private String icon;

    @SerializedName("route")
    private String route;

    protected Notification(Parcel in) {
        id = in.readInt();
        phoneId = in.readInt();
        title = in.readString();
        number = in.readString();
        service = in.readString();
        icon = in.readString();
        route = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public String getTitle() {
        return title;
    }

    public String getNumber() {
        return number;
    }

    public String getService() {
        return service;
    }

    public String getIcon() {
        return icon;
    }

    public String getRoute() {
        return route;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(phoneId);
        parcel.writeString(title);
        parcel.writeString(number);
        parcel.writeString(service);
        parcel.writeString(icon);
        parcel.writeString(route);
    }
}
