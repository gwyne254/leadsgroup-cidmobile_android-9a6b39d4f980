package com.bauart.calleridreputation.json.dashboard;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Dashboard {
    @SerializedName("phones_total_count")
    private int phonesTotalCount;

    @SerializedName("phones_flagged_count")
    private int phonesFlaggedCount;

    @SerializedName("phones_clean_count")
    private int phonesCleanCount;

    @SerializedName("phones_flagged_count_day")
    private int phonesFlaggedCountDay;

    @SerializedName("phones_flagged_count_week")
    private int phoneFlaggedCountWeek;

    @SerializedName("phones_flagged_count_month")
    private int phonesFlaggedCountMonth;

    @SerializedName("phones_archived_count")
    private int phonesArchivedCount;

    @SerializedName("last_10_flagged_phones")
    private ArrayList<FlaggedPhone> flaggedPhones;

    @SerializedName("colors_and_letters")
    private ArrayList<Service> services;

    public int getPhonesTotalCount() {
        return phonesTotalCount;
    }

    public int getPhonesFlaggedCount() {
        return phonesFlaggedCount;
    }

    public int getPhonesCleanCount() {
        return phonesCleanCount;
    }

    public int getPhonesFlaggedCountDay() {
        return phonesFlaggedCountDay;
    }

    public int getPhoneFlaggedCountWeek() {
        return phoneFlaggedCountWeek;
    }

    public int getPhonesFlaggedCountMonth() {
        return phonesFlaggedCountMonth;
    }

    public int getPhonesArchivedCount() {
        return phonesArchivedCount;
    }

    public ArrayList<FlaggedPhone> getFlaggedPhones() {
        return flaggedPhones;
    }

    public ArrayList<Service> getServices() {
        return services;
    }
}
