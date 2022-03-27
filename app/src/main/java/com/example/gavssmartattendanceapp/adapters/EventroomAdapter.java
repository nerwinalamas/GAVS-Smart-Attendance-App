package com.example.gavssmartattendanceapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gavssmartattendanceapp.R;
import com.example.gavssmartattendanceapp.models.Events;

import java.util.ArrayList;

public class EventroomAdapter extends RecyclerView.Adapter<EventroomAdapter.CreateViewHolder2> {
    private ArrayList<Events> eventsList;

    private OnItemClickListener onItemClickListener2;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener2(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener2 = onItemClickListener2;
    }

    public EventroomAdapter(ArrayList<Events> eventsList){
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public CreateViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_create_item2, parent, false);
        return new CreateViewHolder2(view, onItemClickListener2);
    }

    @Override
    public void onBindViewHolder(@NonNull final CreateViewHolder2 holder, final int position) {
        Events events = eventsList.get(position);
        holder.id.setText(events.getEventId());
        holder.eventName.setText(events.getName());
        holder.eventLocation.setText(events.getLocation());
        holder.time.setText(events.getTime());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    class CreateViewHolder2 extends RecyclerView.ViewHolder {

        TextView eventName, eventLocation, time, id;

        CreateViewHolder2(@NonNull View itemView, OnItemClickListener onItemClickListener2) {
            super(itemView);
            id = itemView.findViewById(R.id.id_class);
            eventName = itemView.findViewById(R.id.event_name);
            eventLocation = itemView.findViewById(R.id.event_location);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(v -> onItemClickListener2.onClick(getAdapterPosition()));
        }
    }
}
