package com.bauart.calleridreputation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.json.defaultResponse.DefaultResponse;
import com.bauart.calleridreputation.json.notifications.NotificationData;
import com.bauart.calleridreputation.server.Server;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = NotificationActivity.class.getSimpleName();

    private NotificationData notificationData;

    private TextView phone;
    private TextView service;
    private TextView description;
    private TextView createdAt;
    private TextView readAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notificationData = getIntent().getParcelableExtra("notification");
        if (notificationData != null) {
            getSupportActionBar().setTitle(getString(R.string.notification_title, notificationData.getNotification().getId()));
        }

        initializeGUI();
        displayNotification();
        markAsRead();
    }

    private void initializeGUI() {
        phone = findViewById(R.id.phone);
        service = findViewById(R.id.service);
        description = findViewById(R.id.description);
        createdAt = findViewById(R.id.created_at);
        readAt = findViewById(R.id.read_at);
    }

    private void displayNotification() {
        phone.setText(notificationData.getNotification().getNumber());
        service.setText(notificationData.getNotification().getService());
        description.setText(notificationData.getNotification().getTitle());
        createdAt.setText(notificationData.getCreatedAt());
        readAt.setText(notificationData.getReadAt());
    }

    private void markAsRead() {
        Server.markNotificationAsRead(notificationData.getId(), new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Log.e(TAG, "Code: " + response.code());
                Log.e(TAG, "Message: " + response.message());
                if (!response.isSuccessful()) {
                    Log.e(TAG, getString(R.string.response_isnt_successful));
                    return;
                }
                if (response.body() == null) {
                    Log.e(TAG, getString(R.string.the_body_is_empty));
                    return;
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void notifyUser(String message) {
        Snackbar.make(phone, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
