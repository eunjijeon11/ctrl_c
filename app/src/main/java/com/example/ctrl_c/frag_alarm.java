package com.example.ctrl_c;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class frag_alarm extends Fragment {

    View view;
    RecyclerView alarmRecyclerView;
    alarm_RecyclerViewAdapter a_Rvadapter;
    Button btn_add;
    DBOpenHelper dbOpenHelper;
    ArrayList<Long> arrayIndex = new ArrayList<>();

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
                intent.putExtra("Change type", "add");
                startActivityForResult(intent,1000);
            }
        });

        a_Rvadapter.setOnItemClickListener(new alarm_RecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onChangeClick(int position) {
                Intent intent = new Intent(getActivity(),alarm_setting.class);
                intent.putExtra("Change type", "change");
                intent.putExtra("alarm id", arrayIndex.get(position));
                intent.putExtra("alarm Name", a_Rvadapter.items.get(position).getAlarmName());
                intent.putExtra("alarm Hour", a_Rvadapter.items.get(position).getHour());
                intent.putExtra("alarm Min", a_Rvadapter.items.get(position).getMin());
                startActivityForResult(intent,1000);
            }

            @Override
            public void onDeleteClick(int position) {
                a_Rvadapter.items.remove(position);
                a_Rvadapter.notifyDataSetChanged();
            }
        });


        dbOpenHelper = new DBOpenHelper(getActivity());
        dbOpenHelper.open("alarm");

        Cursor cursor = dbOpenHelper.selectColumns("alarm");
        while (cursor.moveToNext()) {
            long tempIndex = cursor.getLong(cursor.getColumnIndex("_id"));
            String tempAlarmName = cursor.getString(cursor.getColumnIndex("alarm name"));
            int tempHour = cursor.getInt(cursor.getColumnIndex("alarm hour"));
            int tempMin = cursor.getInt(cursor.getColumnIndex("alarm min"));
            boolean tempOnOff = (cursor.getInt(cursor.getColumnIndex("onOff")) == 1); //숫자인 onOff를 boolean으로 만들기

            //...가져온 데이터를 이용하기(리사이클러뷰에 추가하기 등등)...
            AlarmData tempData = new AlarmData();
            tempData.setAlarmName(tempAlarmName);
            tempData.setHour(tempHour);
            tempData.setMin(tempMin);
            tempData.setOnOff(tempOnOff);
            a_Rvadapter.addItem(tempData);

            //id 저장하기
            arrayIndex.add(tempIndex);
        }
        cursor.close();


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
                    a_Rvadapter.items.add(arrayIndex.indexOf(data.getIntExtra("alarm position", 0)), tempData);
                    a_Rvadapter.notifyDataSetChanged();
                }
            }
        }
    }


}
