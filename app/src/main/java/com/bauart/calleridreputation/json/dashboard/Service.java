package com.bauart.calleridreputation.json.dashboard;

import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("name")
    private String name;

    @SerializedName("count")
    private String count;

    @SerializedName("letter")
    private String letter;

    @SerializedName("color")
    private String color;

    @SerializedName("order")
    private String order;

    public Service(String name, String count, String letter, String color, String order) {
        this.name = name;
        this.count = count;
        this.letter = letter;
        this.color = color;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public String getLetter() {
        return letter;
    }

    public String getColor() {
        return color;
    }

    public String getOrder() {
        return order;
    }
}
