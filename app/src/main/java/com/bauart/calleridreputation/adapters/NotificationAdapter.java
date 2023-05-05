package com.bauart.calleridreputation.adapters;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.activities.NotificationActivity;
import com.bauart.calleridreputation.json.notifications.NotificationData;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private ArrayList<NotificationData> notifications = new ArrayList<>();

    public void setNotifications(ArrayList<NotificationData> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public void addNotifications(ArrayList<NotificationData> notifications) {
        this.notifications.addAll(notifications);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(notifications.get(position), position);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text;
        private TextView date;
        private ImageView pic;
        private NotificationData notification;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            text = itemView.findViewById(R.id.notification_text);
            date = itemView.findViewById(R.id.notification_date);
            pic = itemView.findViewById(R.id.new_pic);
        }

        private void bind(NotificationData notification, int position) {
            this.notification = notification;
            text.setText(notification.getNotification().getTitle());
            date.setText(notification.getCreatedAt());
            if (notification.getReadAt() == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    text.setTextAppearance(R.style.TextStyleBold);
                    date.setTextAppearance(R.style.TextStyleBold);
                } else {
                    text.setTypeface(null, Typeface.BOLD);
                    date.setTypeface(null, Typeface.BOLD);
                }

                pic.setVisibility(View.VISIBLE);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    text.setTextAppearance(R.style.TextStyleDefault);
                    date.setTextAppearance(R.style.TextStyleDefault);
                } else {
                    text.setTypeface(null, Typeface.NORMAL);
                    date.setTypeface(null, Typeface.NORMAL);
                }
                pic.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            itemView.getContext().startActivity(new Intent(itemView.getContext(), NotificationActivity.class)
                .putExtra("notification", notification));
        }
    }
}
