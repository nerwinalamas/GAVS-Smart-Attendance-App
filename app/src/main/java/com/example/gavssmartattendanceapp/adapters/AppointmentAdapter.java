package com.example.gavssmartattendanceapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gavssmartattendanceapp.R;
import com.example.gavssmartattendanceapp.models.HealthForm;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private ArrayList<HealthForm> healthForms;
    private OnItemClickListener onItemClickListener;

    public AppointmentAdapter(ArrayList<HealthForm> healthForms, OnItemClickListener onItemClickListener) {
        this.healthForms = healthForms;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_room, parent, false);
        return new AppointmentViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        HealthForm healthForm = healthForms.get(position);
        holder.bind(healthForm);
//        holder.bind(e.getUser());
//        e.getAppointments();
//        User user = e.getUser();
//        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return healthForms.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRoomName, tvSubName, tvTime;
        OnItemClickListener onItemClickListener;

        public AppointmentViewHolder(@NonNull View v, OnItemClickListener onItemClickListener) {
            super(v);
            tvRoomName = v.findViewById(R.id.tv_rname_page2);
            tvSubName = v.findViewById(R.id.tv_sname_page2);
            tvTime = v.findViewById(R.id.tv_time_page2);
            this.onItemClickListener = onItemClickListener;

            v.setOnClickListener(this);
        }

        public void bind(HealthForm healthForm){
            tvRoomName.setText("Name: " + healthForm.getHfName());
            tvSubName.setText("Temperature: " + healthForm.getHfTemperature());
            tvTime.setText("Purpose of Visit: " + healthForm.getHfPurposeOfVisit());
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
}
