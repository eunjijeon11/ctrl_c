package com.example.ctrl_c;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/******************************************************
 * ACTIVITY: 과목을 추가하거나 수정할 수 있는 화면
 * TODO
 * #리사이클러뷰로 과목리스트 보여줌
 * #수정 버튼을 누르면 다이얼로그를 띄우고 값 가져와서 반영.
 * #(원래 값 들어있어야함)
 * #추가 버튼을 누르면 다이얼로그를 띄우고 값 가져와서 반영.
 * #삭제 버튼을 누르면 해당 아이템을 삭제.
 * 과목명, ID, PW를 담은 SQLite 생성
 * SQLite 연동
 * 앱을 들어올때마다 SQLite에서 데이터를 가져와서 표시해야함.
 * frag_timetable에서 데이터 반영!
 *******************************************************/

public class subject_setting extends AppCompatActivity {

    RecyclerView recyclerView;
    subject_RecyclerViewAdapter recyclerviewAdapter;
    Button btn_add;

    Dialog dialog1;
    EditText et_subject, et_ID, et_PW;
    Button btn_cancel1, btn_next;

    Dialog dialog2;
    NumberPicker np_alarmTime;
    Button btn_cancel2, btn_ok;
    CheckBox cb_alarm;
    LinearLayout linearLayout;

    int ADDITEM = 0;
    int CHANGEITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_setting);

        //TODO: findviewById 추가하기!

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerviewAdapter = new subject_RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerviewAdapter);

        for (int i=0; i<3; i++) {
            addItem("수학", "010 6338 9793", "12345", 5, false);
        }

        setDialog();

        recyclerviewAdapter.setOnItemClickListener(new subject_RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onChangeClick(int position) {
                changeSubjectInfo(position);
            }

            @Override
            public void onDeleteClick(int position) {
                recyclerviewAdapter.items.remove(position);
                recyclerviewAdapter.notifyDataSetChanged();
            }

        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSubjectInfo();
            }
        });
    }

    public void addItem(String subject, String ID, String PW, int alarmTime, Boolean useAlarm) {
        Data data = new Data();
        data.setSubject(subject);
        data.setID(ID);
        data.setPW(PW);
        data.setAlarmTime(alarmTime);
        data.setUseAlarm(useAlarm);
        recyclerviewAdapter.addItem(data);
        recyclerviewAdapter.notifyDataSetChanged();
    }

    public void changeItem(int position, String subject, String ID, String PW, int alarmTime, Boolean useAlarm) {
        recyclerviewAdapter.items.remove(position);
        Data data = new Data();
        data.setSubject(subject);
        data.setID(ID);
        data.setPW(PW);
        data.setAlarmTime(alarmTime);
        data.setUseAlarm(useAlarm);
        recyclerviewAdapter.items.add(position, data);
        recyclerviewAdapter.notifyDataSetChanged();
    }

    public void createSubjectInfo() {
        et_subject.setText("");
        et_ID.setText("");
        et_PW.setText("");
        cb_alarm.setChecked(false);
        np_alarmTime.setValue(0);
        runDialog(0, 0);
    }

    public void changeSubjectInfo(final int position) {
        Data recentData = recyclerviewAdapter.items.get(position);
        et_subject.setText(recentData.getSubject());
        et_ID.setText(recentData.getID());
        et_PW.setText(recentData.getPW());
        cb_alarm.setChecked(recentData.getUseAlarm());
        np_alarmTime.setValue(recentData.getAlarmTime());

        runDialog(position, CHANGEITEM);
    }

    void runDialog(final int position, final int changetype) {
        dialog1.show();

        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();

                final String subject = et_subject.getText().toString();
                final String ID = et_ID.getText().toString();
                final String PW = et_PW.getText().toString();

                cb_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (cb_alarm.isChecked()) {
                            linearLayout.setVisibility(View.VISIBLE);
                        } else {
                            linearLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                btn_cancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean useAlarm = false;
                        if (cb_alarm.isChecked()) useAlarm = true;
                        int alarmTime = np_alarmTime.getValue();
                        if (changetype == 0) {
                            addItem(subject, ID, PW, alarmTime, useAlarm);
                        } else if (changetype == 1) {
                            changeItem(position, subject, ID, PW, alarmTime, useAlarm);
                        }
                        dialog2.dismiss();
                    }
                });
                dialog1.dismiss();
            }
        });
    }

    void setDialog() {
        dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.timetable_dialog1);
        dialog1.setOwnerActivity(subject_setting.this);
        setDialogSize(dialog1);

        dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.timetable_dialog2);
        dialog2.setOwnerActivity(subject_setting.this);
        setDialogSize(dialog2);

        et_subject = dialog1.findViewById(R.id.et_subject);
        et_ID = dialog1.findViewById(R.id.et_ID);
        et_PW = dialog1.findViewById(R.id.et_PW);
        btn_cancel1 = dialog1.findViewById(R.id.btn_cancel);
        btn_next = dialog1.findViewById(R.id.btn_next);

        cb_alarm = dialog2.findViewById(R.id.cb_alarm);
        np_alarmTime = dialog2.findViewById(R.id.np_alarmTime);
        btn_cancel2 = dialog2.findViewById(R.id.btn_cancel2);
        btn_ok = dialog2.findViewById(R.id.btn_ok);
        linearLayout = dialog2.findViewById(R.id.linearLDialog);
        np_alarmTime.setMinValue(0);
        np_alarmTime.setMaxValue(20);
    }

    void setDialogSize(Dialog dialog) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = dialog.getWindow();
        int x = (int)(size.x * 1.0f);
        int y = (int)(size.y * 0.4f);
        window.setLayout(x, y);
    }
}
