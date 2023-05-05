package com.bauart.calleridreputation.json.notifications;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NotificationData implements Parcelable {
    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("notifiable_type")
    private String notifiableType;

    @SerializedName("notifiable_id")
    private int notifiableId;

    @SerializedName("read_at")
    private String readAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("data")
    private Notification notification;

    protected NotificationData(Parcel in) {
        id = in.readString();
        type = in.readString();
        notifiableType = in.readString();
        notifiableId = in.readInt();
        readAt = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        notification = in.readParcelable(Notification.class.getClassLoader());
    }

    public static final Creator<NotificationData> CREATOR = new Creator<NotificationData>() {
        @Override
        public NotificationData createFromParcel(Parcel in) {
            return new NotificationData(in);
        }

        @Override
        public NotificationData[] newArray(int size) {
            return new NotificationData[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getNotifiableType() {
        return notifiableType;
    }

    public int getNotifiableId() {
        return notifiableId;
    }

    public String getReadAt() {
        return readAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Notification getNotification() {
        return notification;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(type);
        parcel.writeString(notifiableType);
        parcel.writeInt(notifiableId);
        parcel.writeString(readAt);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeParcelable(notification, i);
    }
}
