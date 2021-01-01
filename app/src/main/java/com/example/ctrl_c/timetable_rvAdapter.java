package com.example.ctrl_c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class timetable_rvAdapter extends RecyclerView.Adapter<timetable_rvAdapter.ViewHolder> {

    ArrayList<SubjectData> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_recyclerview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subject;
        CardView cv_subject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subject = itemView.findViewById(R.id.tv_timetable);
            cv_subject = itemView.findViewById(R.id.cv_timetable);
        }
        public void onBind(SubjectData classInfo) {
            if (!classInfo.getSubject().equals("NONE")) {
                tv_subject.setText(classInfo.getSubject());
            } else {
                tv_subject.setText("");
            }

            cv_subject.setCardBackgroundColor(classInfo.getColor());
        }
    }

    public void addItem(SubjectData classInfo) {
        items.add(classInfo);
    }
}
