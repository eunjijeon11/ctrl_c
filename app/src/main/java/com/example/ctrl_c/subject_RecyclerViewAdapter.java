package com.example.ctrl_c;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class subject_RecyclerViewAdapter extends RecyclerView.Adapter<subject_RecyclerViewAdapter.ViewHolder> {

    public ArrayList<SubjectData> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCopyClick(int position);
        void onChangeClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subject;
        TextView tv_ID;
        TextView tv_PW;
        TextView tv_alarmBefore;
        ImageButton btn_copy, btn_delete;
        Boolean useAlarm;
        String ID, PW, alarmBefore, color;
        CardView cv_background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_ID = itemView.findViewById(R.id.tv_ID);
            tv_PW = itemView.findViewById(R.id.tv_PW);
            tv_alarmBefore = itemView.findViewById(R.id.tv_alarmBefore);
            btn_copy = itemView.findViewById(R.id.btn_copy);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            cv_background = itemView.findViewById(R.id.cv_subject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onChangeClick(position);
                        }
                    }
                }
            });

            btn_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCopyClick(position);
                        }
                    }
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

        void onbind(SubjectData subjectData) {
            alarmBefore = subjectData.getAlarmBefore() + "분 전에 알림";
            ID = "ID: " + subjectData.getID();
            PW = "PW: " + subjectData.getPW();
            color = subjectData.getColor();

            cv_background.setCardBackgroundColor(Color.parseColor(color));
            tv_subject.setText(subjectData.getSubject());
            tv_ID.setText(ID);
            tv_PW.setText(PW);
            tv_alarmBefore.setText(alarmBefore);
            useAlarm = subjectData.getUseAlarm();

            if (!useAlarm) {
                tv_alarmBefore.setVisibility(View.INVISIBLE);
            } else if (useAlarm) {
                tv_alarmBefore.setVisibility(View.VISIBLE);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_recyclerview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onbind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean addItem(SubjectData subjectData) {
        items.add(subjectData);
        return true;
    }
}
