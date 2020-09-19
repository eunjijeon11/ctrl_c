package com.example.ctrl_c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class subject_RecyclerViewAdapter extends RecyclerView.Adapter<subject_RecyclerViewAdapter.ViewHolder> {

    public ArrayList<Data> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
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
        TextView tv_alarmTime;
        Button btn_change, btn_delete;
        Boolean useAlarm;
        String ID, PW, alarmTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_ID = itemView.findViewById(R.id.tv_ID);
            tv_PW = itemView.findViewById(R.id.tv_PW);
            tv_alarmTime = itemView.findViewById(R.id.tv_alarmTime);
            btn_change = itemView.findViewById(R.id.btn_change);
            btn_delete = itemView.findViewById(R.id.btn_delete);

            btn_change.setOnClickListener(new View.OnClickListener() {
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

        void onbind(Data data) {
            alarmTime = data.getAlarmTime() + "Min";
            ID = "ID: " + data.getID();
            PW = "PW: " + data.getPW();

            tv_subject.setText(data.getSubject());
            tv_ID.setText(ID);
            tv_PW.setText(PW);
            tv_alarmTime.setText(alarmTime);
            useAlarm = data.getUseAlarm();

            if (!useAlarm) {
                tv_alarmTime.setVisibility(View.INVISIBLE);
            } else if (useAlarm) {
                tv_alarmTime.setVisibility(View.VISIBLE);
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

    void addItem(Data data) {
        items.add(data);
    }
}
