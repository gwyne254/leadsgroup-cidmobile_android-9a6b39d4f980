package com.bauart.calleridreputation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.adapters.SettingNotificationAdapter;
import com.bauart.calleridreputation.json.defaultResponse.DefaultResponse;
import com.bauart.calleridreputation.json.notificationSettings.SettingsNotificationResponse;
import com.bauart.calleridreputation.server.Server;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsNotificationsActivity extends AppCompatActivity implements SettingNotificationAdapter.SettingsNotificationsDelegate {

    private static final String TAG = SettingsNotificationsActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private SettingNotificationAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_notifications);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SettingNotificationAdapter();
        adapter.setDelegate(this);
        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBar);

        getNotificationSettings();
    }

    private void getNotificationSettings() {
        progressBar.setVisibility(View.VISIBLE);
        Server.getSettingsNotifications(new Callback<SettingsNotificationResponse>() {
            @Override
            public void onResponse(Call<SettingsNotificationResponse> call, Response<SettingsNotificationResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Code: " + response.code());
                Log.e(TAG, "Message: " + response.message());
                if (!response.isSuccessful()) {
                    Log.e(TAG, getString(R.string.response_isnt_successful));
                    notifyUser("[NS-RE-01] Looks like we had an issue attempting to load this. Try again later.");
                    return;
                }
                if (response.body() == null) {
                    Log.e(TAG, getString(R.string.the_body_is_empty));
                    notifyUser("[NS-RQ-E] No content!");
                    return;
                }

                adapter.setSettingNotifications(response.body().getSettings().getSetting());
            }

            @Override
            public void onFailure(Call<SettingsNotificationResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notifyUser("[NS-OE] Whoops! That wasn't supposed to happen. Try again or contact support.");
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void settingChanged(String key, boolean email, boolean push) {
        Server.saveSettingsNotifications(key.substring(7), push ? "1" : "0", email ? "1" : "0", new Callback<DefaultResponse>() {
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
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
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
