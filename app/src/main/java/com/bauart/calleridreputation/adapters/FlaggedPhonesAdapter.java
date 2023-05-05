package com.bauart.calleridreputation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bauart.calleridreputation.R;
import com.bauart.calleridreputation.json.dashboard.FlaggedPhone;

import java.util.ArrayList;

public class FlaggedPhonesAdapter extends RecyclerView.Adapter<FlaggedPhonesAdapter.FlaggedPhoneViewHolder> {

    private ArrayList<FlaggedPhone> phones = new ArrayList<>();

    public void setPhones(ArrayList<FlaggedPhone> phones) {
        this.phones = phones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlaggedPhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FlaggedPhoneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.flagged_phone_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FlaggedPhoneViewHolder holder, int position) {
        holder.bind(phones.get(position), position);
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    class FlaggedPhoneViewHolder extends RecyclerView.ViewHolder {

        private TextView phone;
        private TextView date;

        private FlaggedPhone flaggedPhone;

        public FlaggedPhoneViewHolder(@NonNull View itemView) {
            super(itemView);

            phone = itemView.findViewById(R.id.phone);
            date = itemView.findViewById(R.id.date);
        }

        private void bind(FlaggedPhone flaggedPhone, int position) {
            this.flaggedPhone = flaggedPhone;
            phone.setText(flaggedPhone.getNumber());
            date.setText(flaggedPhone.getDateFirstFlagged());

            if (position % 2 == 1) {
                itemView.setBackground(itemView.getContext().getDrawable(R.color.colorSmoke));
            }
        }
    }
}
