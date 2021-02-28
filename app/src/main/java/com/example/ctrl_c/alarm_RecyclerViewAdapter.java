package com.example.ctrl_c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

public class alarm_RecyclerViewAdapter extends RecyclerView.Adapter<alarm_RecyclerViewAdapter.ViewHolder> {

    private alarm_RecyclerViewAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onChangeClick(int position);
        void onDeleteClick(int position);
        void onCheckSwitch(int position, boolean isChecked);
    }

    public void setOnItemClickListener(alarm_RecyclerViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    ArrayList<AlarmData> items = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_recyclerview, parent, false);
        return new alarm_RecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onbind(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_alarmName, tv_alarmTime;
        SwitchCompat Sc_alarmSwitch;
        Button btn_alarmDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_alarmName = itemView.findViewById(R.id.tv_alarmName);
            tv_alarmTime = itemView.findViewById(R.id.tv_alarmTime);
            Sc_alarmSwitch = itemView.findViewById(R.id.alarmSwitch);
            btn_alarmDelete = itemView.findViewById(R.id.btn_alarmDelete);

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

            btn_alarmDelete.setOnClickListener(new View.OnClickListener() {
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

            Sc_alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCheckSwitch(position, isChecked);
                        }
                    }
                }
            });

        }
        public void onbind(AlarmData alarmData){
            tv_alarmName.setText(alarmData.getAlarmName());
            tv_alarmTime.setText(alarmData.getHour()+":"+alarmData.getMin());
            Sc_alarmSwitch.setChecked(alarmData.getOnOff());
        }

    }
    public boolean addItem(AlarmData alarmData) {
        items.add(alarmData);
        return true;
    }
}
