package com.example.gavssmartattendanceapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gavssmartattendanceapp.R;
import com.example.gavssmartattendanceapp.models.Classroom;

import java.util.ArrayList;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.CreateHolder> {
    ArrayList<Classroom> classroomsArrayList;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public  ClassroomAdapter(ArrayList<Classroom> classroomsArrayList){
        this.classroomsArrayList = classroomsArrayList;
    }

    @NonNull
    @Override
    public CreateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_create_item, parent, false);
        return new CreateHolder(v, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateHolder holder, int position) {
        Classroom classrooms = classroomsArrayList.get(position);
        holder.id.setText(classrooms.getClassId());
        holder.className.setText(classrooms.getName());
        holder.subjectCode.setText(classrooms.getSubjectCode());
        holder.section.setText(classrooms.getSection());
    }

    @Override
    public int getItemCount() {
        return classroomsArrayList.size();
    }

    class CreateHolder extends RecyclerView.ViewHolder {

        TextView id, className, subjectCode, section;

        public CreateHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            id = itemView.findViewById(R.id.id_class2);
            className = itemView.findViewById(R.id.class_name);
            subjectCode = itemView.findViewById(R.id.subject_code);
            section = itemView.findViewById(R.id.section);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }
}
