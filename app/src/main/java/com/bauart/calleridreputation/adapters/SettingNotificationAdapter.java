package com.bauart.calleridreputation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.json.notificationSettings.SettingNotification;

import java.util.ArrayList;

public class SettingNotificationAdapter extends RecyclerView.Adapter<SettingNotificationAdapter.SettingViewHolder> {

    private ArrayList<SettingNotification> settingNotifications = new ArrayList<>();
    private SettingsNotificationsDelegate delegate;

    public void setSettingNotifications(ArrayList<SettingNotification> settingNotifications) {
        this.settingNotifications = settingNotifications;
        notifyDataSetChanged();
    }

    public void setDelegate(SettingsNotificationsDelegate delegate) {
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        holder.bind(settingNotifications.get(position), position);
    }

    @Override
    public int getItemCount() {
        return settingNotifications.size();
    }

    class SettingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private TextView textView;
        private SwitchCompat push;
        private SwitchCompat email;
        private LinearLayout emailLayout;
        private LinearLayout pushLayout;

        private SettingNotification settingNotification;

        public SettingViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.setting_name);
            push = itemView.findViewById(R.id.push_switch);
            email = itemView.findViewById(R.id.email_switch);

            emailLayout = itemView.findViewById(R.id.email_layout);
            pushLayout = itemView.findViewById(R.id.push_layout);

            emailLayout.setOnClickListener(this);
            pushLayout.setOnClickListener(this);
        }

        private void bind(SettingNotification settingNotification, int position) {
            this.settingNotification = settingNotification;
            textView.setText(settingNotification.getName());

            push.setOnCheckedChangeListener(null);
            email.setOnCheckedChangeListener(null);
            push.setChecked(settingNotification.getPush().equals("1"));
            email.setChecked(settingNotification.getEmail().equals("1"));
            push.setOnCheckedChangeListener(this);
            email.setOnCheckedChangeListener(this);

            if (position % 2 == 1) {
                itemView.setBackground(itemView.getContext().getDrawable(R.color.colorSmoke));
            }
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == pushLayout.getId()) {
                push.setChecked(!push.isChecked());
            } else if (view.getId() == emailLayout.getId()) {
                email.setChecked(!email.isChecked());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (delegate == null) {
                return;
            }
            delegate.settingChanged(settingNotification.getKey(), email.isChecked(), push.isChecked());

        }
    }

    public interface SettingsNotificationsDelegate {
        void settingChanged(String key, boolean email, boolean push);
    }
}