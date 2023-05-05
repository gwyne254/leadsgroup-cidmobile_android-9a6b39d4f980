package com.bauart.calleridreputation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bauart.calleridreputation.activities.AuthActivity;
import com.bauart.calleridreputation.activities.SettingsNotificationsActivity;
import com.bauart.calleridreputation.fragments.DashboardFragment;
import com.bauart.calleridreputation.fragments.NotificationsFragment;
import com.bauart.calleridreputation.fragments.ProfileFragment;
import com.bauart.calleridreputation.json.defaultResponse.DefaultResponse;
import com.bauart.calleridreputation.server.Server;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int AUTH_REQUEST = 1010;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String token;
    private static String firebaseToken;

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        token = sharedPreferences.getString(Constants.TOKEN, null);
        if (token == null) {
            requestAuth();
        } else {
            getFirebaseToken();
        }

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void requestAuth() {
        startActivityForResult(new Intent(this, AuthActivity.class), AUTH_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_REQUEST) {
            if (resultCode == RESULT_OK) {
                // good
                String token = data.getStringExtra(Constants.TOKEN);
                if (token != null) {
                    Log.e(TAG, token);
                    MainActivity.token = token;
                    editor.putString(Constants.TOKEN, token);
                    editor.commit();
                }
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    private void signOut() {
        editor.remove(Constants.TOKEN);
        editor.commit();
        token = null;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.sign_out: signOut(); break;
//            case R.id.settings: showNotificationSettings(); break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNotificationSettings() {
        startActivity(new Intent(this, SettingsNotificationsActivity.class));
    }

    private void getFirebaseToken() {
        FirebaseInstallations.getInstance().getToken(/* forceRefresh */true)
                .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String token = task.getResult().getToken();
                            firebaseToken = token;
                            Log.e(TAG, token);

                            Server.saveFirebaseToken(new Callback<DefaultResponse>() {
                                @Override
                                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                    Log.e(TAG, "Code: " + response.code());
                                    Log.e(TAG, "Message: " + response.message());
                                    if (!response.isSuccessful()) {
                                        Log.e(TAG, "The response isn't successful");
                                    }
                                    if (response.body() == null) {
                                        Log.e(TAG, "The body is empty");
                                        return;
                                    }

                                }

                                @Override
                                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                    Log.e(TAG, t.getLocalizedMessage());
                                }
                            }, firebaseToken);



                            Log.d("Installations", "Installation auth token: " + task.getResult().getToken());
                        } else {
                            Log.e("Installations", "Unable to get Installation auth token");
                        }
                    }
                });

//        FirebaseInstallations.getInstance().getId()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("Installations", "Installation ID: " + task.getResult());
//                        } else {
//                            Log.e("Installations", "Unable to get Installation ID");
//                        }
//                    }
//                });
    }

    private void notifyUser(String message) {
        Snackbar.make(viewPager, message, Snackbar.LENGTH_SHORT).show();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public int getCount() {
            return 3;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new DashboardFragment();
                case 1: return new NotificationsFragment();
                case 2: return new ProfileFragment();
                default: return new DashboardFragment();
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.dashboard);
                case 1: return getString(R.string.notifications);
                case 2: return getString(R.string.profile);
                default: return getString(R.string.dashboard);
            }
        }
    }
}
