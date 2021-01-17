package com.example.ctrl_c;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class timetable_rvAdapter extends RecyclerView.Adapter<timetable_rvAdapter.ViewHolder> {

    ArrayList<SubjectData> items = new ArrayList<>();

    private OnItemClickListener listener = null;
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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

            cv_subject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });

            cv_subject.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        if (!items.get(getAdapterPosition()).getSubject().equals("NONE")) {
                            listener.onItemLongClick(v, getAdapterPosition());
                            tv_subject.setText("");
                            Drawable drawable;
                            drawable = ResourcesCompat.getDrawable(cv_subject.getContext().getResources(), R.drawable.delete_subject, null);
                            v.setBackground(drawable);
                        }
                    }
                    return true;
                }
            });
        }
        public void onBind(SubjectData classInfo) {
            if (!classInfo.getSubject().equals("NONE")) {
                tv_subject.setText(classInfo.getSubject());
            } else {
                tv_subject.setText("");
            }
            GradientDrawable background = (GradientDrawable) ResourcesCompat.getDrawable(cv_subject.getContext().getResources(), R.drawable.stroke, null);
            background.setColor(classInfo.getColor());
            background.setCornerRadius(30);
            background.setStroke(0, Color.parseColor("#FFFFFF"));
            cv_subject.setBackground(background);
        }
    }

    public void addItem(SubjectData classInfo) {
        items.add(classInfo);
    }
}
