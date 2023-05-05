package com.bauart.calleridreputation.server;

import com.bauart.calleridreputation.json.AuthResponse;
import com.bauart.calleridreputation.json.dashboard.Dashboard;
import com.bauart.calleridreputation.json.defaultResponse.DefaultResponse;
import com.bauart.calleridreputation.json.notificationSettings.SettingsNotificationResponse;
import com.bauart.calleridreputation.json.notifications.NotificationResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    String BASE_URL = "https://app.calleridrep.com/api/v1/";
    //String BASE_URL = "https://example.calleridrep.com/api/v1/";
    //String BASE_URL = "https://dev.app.calleridrep.com/api/v1/";


    @POST("login")
    @Headers("Content-Type: application/json")
    Call<AuthResponse> login(@Body JsonObject body);

    @POST("qrlogin")
    @Headers("Content-Type: application/json")
    Call<AuthResponse> qrLogin(@Body JsonObject body);

    @GET("dashboard")
    @Headers("Content-Type: application/json")
    Call<Dashboard> getDashboard(@Header("Authorization") String token);

    @GET("notifications")
    @Headers("Content-Type: application/json")
    Call<NotificationResponse> getNotifications(@Header("Authorization") String token, @Query("page") int page);

    @GET("notifications/view/{id}")
    @Headers("Content-Type: application/json")
    Call<DefaultResponse> markNotificationAsRead(@Header("Authorization") String token, @Path("id") String id);

    @GET("settings/notifySettings")
    @Headers("Content-Type: application/json")
    Call<SettingsNotificationResponse> getNotificationSettings(@Header("Authorization") String token);

    @POST("settings/notifySettings")
    @Headers("Content-Type: application/json")
    Call<DefaultResponse> saveNotificationSetting(@Header("Authorization") String token, @Body JsonObject body);


    @POST("firebaseToken")
    @Headers("Content-Type: application/json")
    Call<DefaultResponse> sendFirebaseToken(@Header ("Authorization") String token, @Body JsonObject body);
}
