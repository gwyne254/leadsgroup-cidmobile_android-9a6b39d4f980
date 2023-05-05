package com.bauart.calleridreputation.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.json.dashboard.Service;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ServiceViewHolder> {

    private ArrayList<Service> services = new ArrayList<>();

    public void setServices(ArrayList<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_service_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.bind(services.get(position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {

        private TextView count;
        private TextView name;
        private TextView letter;
        private Service service;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.service_count);
            name = itemView.findViewById(R.id.service_name);
            letter = itemView.findViewById(R.id.service_letter);
        }

        private void bind(Service service) {
            this.service = service;

            ((CardView) itemView).setCardBackgroundColor(Color.parseColor(service.getColor()));
            count.setText(service.getCount());
            name.setText(service.getName());
            letter.setText(service.getLetter());
        }
    }
}
