package com.example.afinal.activity;



import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.DBHelper;
import com.example.afinal.R;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    EditText et_user, et_password;
    Button btn_register, btnRetry;
    DBHelper dbHelper;
    TextView textConnectionLost;
    ProgressBar ProgressBar;
    LinearLayout registerLayout,headerLayout;
    View background;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        et_user = findViewById(R.id.etEmail);
        et_password = findViewById(R.id.etPassword);
        btn_register = findViewById(R.id.btnCreateAccount);
        textConnectionLost = findViewById(R.id.connectionlost);
        btnRetry = findViewById(R.id.btn_retry);
        ProgressBar = findViewById(R.id.ProgressBar);
        registerLayout = findViewById(R.id.allregis);
        headerLayout = findViewById(R.id.allheader);
        background = findViewById(R.id.background_image);

        ProgressBar.setVisibility(View.GONE);
        textConnectionLost.setVisibility(View.GONE);
        btnRetry.setVisibility(View.GONE);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        btnRetry.setOnClickListener(v -> retryNetworkCheck(executor));

        btn_register.setOnClickListener(view -> {
            String user = et_user.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Silakan isi semua bidang", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.isUsernameExists(user)) {
                showProgressAndCheckNetwork(executor,() -> {
                    Toast.makeText(RegisterActivity.this, "Akun sudah terdaftar", Toast.LENGTH_SHORT).show();
                    showRegisterForm();
                });
            } else {
                showProgressAndCheckNetwork(executor, () -> {
                    registerUser(user, password);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                });
            }
        });
    }


    private void showProgressAndCheckNetwork(ExecutorService executor, Runnable onSuccess) {
        ProgressBar.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
        headerLayout.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                ProgressBar.setVisibility(View.GONE);
                if (isNetworkAvailable()) {
                    onSuccess.run();
                } else {
                    showConnectionLost();
                }
            });
        });
    }

    private void showConnectionLost() {
        ProgressBar.setVisibility(View.GONE);
        btnRetry.setVisibility(View.VISIBLE);
        textConnectionLost.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
        headerLayout.setVisibility(View.GONE);
    }

    private void retryNetworkCheck(ExecutorService executor) {
        ProgressBar.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(View.GONE);
        textConnectionLost.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                ProgressBar.setVisibility(View.GONE);
                if (isNetworkAvailable()) {
                    registerLayout.setVisibility(View.VISIBLE);
                    background.setVisibility(View.VISIBLE);
                    headerLayout.setVisibility(View.VISIBLE);
                } else {
                    showConnectionLost();
                }
            });
        });
    }

    private void showRegisterForm() {
        headerLayout.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.VISIBLE);
        background.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(View.GONE);
        textConnectionLost.setVisibility(View.GONE);
    }

    private void registerUser(String user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.UserEntry.COLUMN_NAME_USERNAME, user);
        values.put(DBHelper.UserEntry.COLUMN_NAME_PASSWORD, password);

        long newRowId = db.insert(DBHelper.UserEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Toast.makeText(RegisterActivity.this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegisterActivity.this, "Gagal mendaftar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (isNetworkAvailable()) {
            showConnectionLost();
        } else {
            super.onBackPressed();
        }
    }
}
