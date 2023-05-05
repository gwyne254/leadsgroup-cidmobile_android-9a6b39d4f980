package com.bauart.calleridreputation.fragments;


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
import com.bauart.calleridreputation.adapters.NotificationAdapter;
import com.bauart.calleridreputation.json.notifications.NotificationData;
import com.bauart.calleridreputation.json.notifications.NotificationResponse;
import com.bauart.calleridreputation.server.Server;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {
    private static final String TAG = NotificationsFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    private ArrayList<NotificationData> notifications;
    private int page;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotifications();
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!swipeRefreshLayout.isRefreshing()) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == notifications.size() - 1) {
                        //bottom of list!
                        getNotifications();
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        notifications = null;
        page = 0;
        getNotifications();
    }

    private void getNotifications() {
        swipeRefreshLayout.setRefreshing(true);
        Server.getNotifications(++page, new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
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
                onGetNotificationsSuccess(response.body().getItems().getNotifications());
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                notifyUser(getString(R.string.an_error_occurred));
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void onGetNotificationsSuccess(ArrayList<NotificationData> notificationData) {
        if (notifications == null) {
            notifications = notificationData;
            adapter.setNotifications(notifications);
        } else {
            adapter.addNotifications(notificationData);
        }
    }

    private void notifyUser(String message) {
        if (isAdded()) {
            Snackbar.make(swipeRefreshLayout, message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
