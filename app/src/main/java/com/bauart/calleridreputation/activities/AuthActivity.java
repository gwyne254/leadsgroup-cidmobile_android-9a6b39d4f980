package com.bauart.calleridreputation.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bauart.calleridreputation.Constants;
import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.json.AuthResponse;
import com.bauart.calleridreputation.server.Server;
import com.google.android.material.snackbar.Snackbar;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = AuthActivity.class.getSimpleName();
    public static final int CAMERA_PERMISSION = 4312;
    public static final int QR_CODE = 7612;

    private ProgressBar progressBar;
    private EditText emailEdit;
    private EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        progressBar = findViewById(R.id.progressBar);
        emailEdit = findViewById(R.id.email_edit);
        passwordEdit = findViewById(R.id.password_edit);
    }

    public void login(View view) {
        String email = emailEdit.getText().toString();
        if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
            notifyUser(getString(R.string.wrong_email_format));
            return;
        }

        String password  = passwordEdit.getText().toString();
        if (password.length() < 6) {
            notifyUser(getString(R.string.password_is_too_short));
        }

        progressBar.setVisibility(View.VISIBLE);

        Server.login(email, password, new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Code: " + response.code());
                Log.e(TAG, "Message: " + response.message());
                if (!response.isSuccessful()) {
                    Log.e(TAG, getString(R.string.response_isnt_successful));
                    notifyUser(getString(R.string.response_isnt_successful));
                    return;
                }
                if (response.body() == null) {
                    Log.e(TAG, getString(R.string.the_body_is_empty));
                    notifyUser(getString(R.string.response_is_empty));
                    return;
                }
                notifyUser(getString(R.string.authentication_successful));
                finishAuthentication(response.body().getTokenType() + " " + response.body().getAccessToken());
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notifyUser(getString(R.string.an_error_occurred));
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void finishAuthentication(String token) {
        setResult(RESULT_OK, new Intent().putExtra(Constants.TOKEN, token));
        finish();
    }

    public void qrLogin(View view) {
        //new IntentIntegrator(this).setPrompt(getString(R.string.qr_code_prompt)).setBeepEnabled(false).setCameraId(0).initiateScan();
        if (getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent(this, QRCodeReaderActivity.class), QR_CODE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                qrLogin(null);
            } else {
                notifyUser(getString(R.string.unable_to_open_camera_without_permission));
            }
        }
    }

    //Hey, QR Codes currently do not work on the platform. Cannot continue with this feature.
    private void sendQRToken(String token) {
        progressBar.setVisibility(View.VISIBLE);
        Server.qrLogin(token, new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.e(TAG, "Code: " + response.code());
                Log.e(TAG, "Message: " + response.message());
                if (!response.isSuccessful()) {
                    Log.e(TAG, getString(R.string.response_isnt_successful));
                    notifyUser(getString(R.string.response_isnt_successful));
                    return;
                }
                if (response.body() == null) {
                    Log.e(TAG, getString(R.string.the_body_is_empty));
                    notifyUser(getString(R.string.response_is_empty));
                    return;
                }
                progressBar.setVisibility(View.GONE);
                notifyUser(getString(R.string.authentication_successful));
                finishAuthentication(response.body().getTokenType() + " " + response.body().getAccessToken());
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notifyUser(getString(R.string.an_error_occurred));
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    String code = data.getStringExtra("code");
                    notifyUser(code);
                    sendQRToken(code);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                notifyUser(getString(R.string.cancelled));
            }
        }
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null) {
//            if(result.getContents() == null) {
//                notifyUser(getString(R.string.cancelled));
//            } else {
//                notifyUser(result.getContents());
//                sendQRToken(result.getContents());
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
    }

    public void hideKeyboard(View view) {
        View view1 = this.getCurrentFocus();
        if (view1 != null){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }
    private void notifyUser(String message) {
        Snackbar.make(emailEdit, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        //
        setResult(RESULT_CANCELED);
        finish();
    }
}
