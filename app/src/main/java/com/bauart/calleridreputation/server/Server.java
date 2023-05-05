package com.bauart.calleridreputation.server;

import android.util.Log;

import com.bauart.calleridreputation.MainActivity;
import com.bauart.calleridreputation.json.AuthResponse;
import com.bauart.calleridreputation.json.dashboard.Dashboard;
import com.bauart.calleridreputation.json.defaultResponse.DefaultResponse;
import com.bauart.calleridreputation.json.notificationSettings.SettingsNotificationResponse;
import com.bauart.calleridreputation.json.notifications.NotificationResponse;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bauart.calleridreputation.MainActivity.token;
import static com.bauart.calleridreputation.server.API.BASE_URL;

public class Server {
    private static final String TAG = Server.class.getSimpleName();
    private static API service;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        service = retrofit.create(API.class);
    }

    public static void login(String email, String password, Callback<AuthResponse> callback) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("password", password);
            jsonObject.addProperty("remember_me", "true");
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        service.login(jsonObject).enqueue(callback);
    }

    public static void qrLogin(String qrToken, Callback<AuthResponse> callback) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("qr_token", qrToken);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        service.qrLogin(jsonObject).enqueue(callback);
    }

    public static void getDashboard(Callback<Dashboard> callback) {
        service.getDashboard(token).enqueue(callback);
    }

    public static void getNotifications(int page, Callback<NotificationResponse> callback) {
        service.getNotifications(token, page).enqueue(callback);
    }

    public static void markNotificationAsRead(String id, Callback<DefaultResponse> callback) {
        service.markNotificationAsRead(token, id).enqueue(callback);
    }

    public static void getSettingsNotifications(Callback<SettingsNotificationResponse> callback) {
        service.getNotificationSettings(token).enqueue(callback);
    }

    public static void saveSettingsNotifications(String key, String push, String email, Callback<DefaultResponse> callback) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty(String.format("notify.%s.Email", key), email);
            jsonObject.addProperty(String.format("notify.%s.MobileAppPush", key), push);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        Log.e(TAG, jsonObject.toString());

        service.saveNotificationSetting(token, jsonObject).enqueue(callback);
    }

    public static void saveFirebaseToken(Callback<DefaultResponse> callback, String firebaseToken) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("firebaseToken", firebaseToken);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        Call<DefaultResponse> call = service.sendFirebaseToken(token, jsonObject);
        call.enqueue(callback);
    }
}
