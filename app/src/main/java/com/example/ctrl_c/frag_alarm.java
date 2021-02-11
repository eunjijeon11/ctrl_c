package com.example.ctrl_c;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class frag_alarm extends Fragment {

    View view;
    RecyclerView alarmRecyclerView;
    alarm_RecyclerViewAdapter a_Rvadapter;
    Button btn_add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm, container, false);

        alarmRecyclerView = view.findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        alarmRecyclerView.setLayoutManager(linearLayoutManager);
        a_Rvadapter = new alarm_RecyclerViewAdapter();
        alarmRecyclerView.setAdapter(a_Rvadapter);

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),alarm_setting.class);
                intent.putExtra("Change type", "Add");
                startActivityForResult(intent,1000);
            }
        });

        a_Rvadapter.setOnItemClickListener(new alarm_RecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onChangeClick(int position) {
                Intent intent = new Intent(getActivity(),alarm_setting.class);
                intent.putExtra("Change type", "Change");
                intent.putExtra("change Position",position);
                startActivityForResult(intent,1000);
            }

            @Override
            public void onDeleteClick(int position) {
                a_Rvadapter.items.remove(position);
                a_Rvadapter.notifyDataSetChanged();
            }
        });

        AlarmData alarmData = new AlarmData();
        alarmData.setAlarmName("학교 갈 시간");
        alarmData.setHour(6);
        alarmData.setMin(30);
        alarmData.setOnOff(true);
        a_Rvadapter.addItem(alarmData);
        a_Rvadapter.notifyDataSetChanged();

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1000) {
            if (resultCode == 2000) { //request code와 result code 확인
                if (data.getStringExtra("alarmChangeType").equals("add")) {
                    AlarmData tempData = new AlarmData();
                    tempData.setAlarmName(data.getStringExtra("alarm name"));
                    tempData.setHour(data.getIntExtra("alarm hour", 0));
                    tempData.setMin(data.getIntExtra("alarm min", 0));
                    tempData.setOnOff(true);
                    a_Rvadapter.addItem(tempData);
                    a_Rvadapter.notifyDataSetChanged();
                } else if (data.getStringExtra("alarmChangeType").equals("change")) {
                    AlarmData tempData = new AlarmData();
                    tempData.setAlarmName(data.getStringExtra("alarm name"));
                    tempData.setHour(data.getIntExtra("alarm hour", 0));
                    tempData.setMin(data.getIntExtra("alarm min", 0));
                    tempData.setOnOff(true);
                    a_Rvadapter.items.add(data.getIntExtra("alarm position", 0), tempData);
                    a_Rvadapter.notifyDataSetChanged();
                }
            }
        }
    }
}
