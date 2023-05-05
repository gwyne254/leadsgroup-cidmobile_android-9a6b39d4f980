package com.bauart.calleridreputation.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.activities.LastTenNumbersActivity;
import com.bauart.calleridreputation.adapters.DashboardAdapter;
import com.bauart.calleridreputation.json.dashboard.Dashboard;
import com.bauart.calleridreputation.json.dashboard.Service;
import com.bauart.calleridreputation.server.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private static final String TAG = DashboardFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DashboardAdapter adapter;

    private FloatingActionButton floatingActionButton;

    private Dashboard dashboard;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDashboard();
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DashboardAdapter();
        recyclerView.setAdapter(adapter);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setVisibility(View.GONE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LastTenNumbersActivity.class)
                    .putExtra("last_numbers", dashboard.getFlaggedPhones()));
            }
        });

        getDashboard();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getDashboard() {
        swipeRefreshLayout.setRefreshing(true);
        Server.getDashboard(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
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

                swipeRefreshLayout.setRefreshing(false);
                onGetDashboardSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                notifyUser(getString(R.string.an_error_occurred));
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void onGetDashboardSuccess(Dashboard dashboard) {
        this.dashboard = dashboard;
        ArrayList<Service> services = dashboard.getServices();
        Collections.sort(services, new Comparator<Service>() {
            @Override
            public int compare(Service service, Service t1) {
                return Integer.parseInt(service.getOrder()) - Integer.parseInt(t1.getOrder());
            }
        });
//        services.add(new Service("Phone numbers".toUpperCase(), String.valueOf(dashboard.getPhonesTotalCount()),
//                "*", "#888888", String.valueOf(services.size())));
//        services.add(new Service("Clean numbers".toUpperCase(), String.valueOf(dashboard.getPhonesCleanCount()),
//                "*", "#888888", String.valueOf(services.size())));
//        services.add(new Service("Flagged numbers".toUpperCase(), String.valueOf(dashboard.getPhonesFlaggedCount()),
//                "*", "#888888", String.valueOf(services.size())));
//        services.add(new Service("Archived (all)".toUpperCase(), String.valueOf(dashboard.getPhonesArchivedCount()),
//                "*", "#888888", String.valueOf(services.size())));
//        services.add(new Service("Flagged last 24 hours".toUpperCase(), String.valueOf(dashboard.getPhonesFlaggedCountDay()),
//                "*", "#888888", String.valueOf(services.size())));
//        services.add(new Service("Flagged last 7 days".toUpperCase(), String.valueOf(dashboard.getPhoneFlaggedCountWeek()),
//                "*", "#888888", String.valueOf(services.size())));
//        services.add(new Service("Flagged last 30 days".toUpperCase(), String.valueOf(dashboard.getPhonesFlaggedCountMonth()),
//                "*", "#888888", String.valueOf(services.size())));

        adapter.setServices(services);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    private void notifyUser(String message) {
        if (isAdded()) {
            Snackbar.make(swipeRefreshLayout, message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
