package com.bauart.calleridreputation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.adapters.FlaggedPhonesAdapter;
import com.bauart.calleridreputation.json.dashboard.FlaggedPhone;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class LastTenNumbersActivity extends AppCompatActivity {

    private static final String TAG = LastTenNumbersActivity.class.getSimpleName();

    private ArrayList<FlaggedPhone> phones;

    private RecyclerView recyclerView;
    private FlaggedPhonesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_ten_numbers);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initGUI();

        phones = getIntent().getParcelableArrayListExtra("last_numbers");
        if (phones == null) {
            notifyUser(getString(R.string.unable_to_get_last_10_flagged_phones));
        } else {
            displayLastPhones();
        }
    }

    private void initGUI() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FlaggedPhonesAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void displayLastPhones() {
        adapter.setPhones(phones);
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
