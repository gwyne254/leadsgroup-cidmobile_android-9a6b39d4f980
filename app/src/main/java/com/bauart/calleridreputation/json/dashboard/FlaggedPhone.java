package com.bauart.calleridreputation.json.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FlaggedPhone implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("number")
    private String number;

    @SerializedName("phone_id")
    private int phoneId;

    @SerializedName("state_code")
    private String stateCode;

    @SerializedName("date_first_flagged")
    private String dateFirstFlagged;

    protected FlaggedPhone(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        number = in.readString();
        phoneId = in.readInt();
        stateCode = in.readString();
        dateFirstFlagged = in.readString();
    }

    public static final Creator<FlaggedPhone> CREATOR = new Creator<FlaggedPhone>() {
        @Override
        public FlaggedPhone createFromParcel(Parcel in) {
            return new FlaggedPhone(in);
        }

        @Override
        public FlaggedPhone[] newArray(int size) {
            return new FlaggedPhone[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getNumber() {
        return number;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getDateFirstFlagged() {
        return dateFirstFlagged;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(userId);
        parcel.writeString(number);
        parcel.writeInt(phoneId);
        parcel.writeString(stateCode);
        parcel.writeString(dateFirstFlagged);
    }
}
