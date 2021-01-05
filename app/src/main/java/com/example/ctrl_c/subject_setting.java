package com.example.ctrl_c;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import petrov.kristiyan.colorpicker.ColorPicker;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

/******************************************************
 * ACTIVITY: 과목을 추가하거나 수정할 수 있는 화면
 * TODO
 * #리사이클러뷰로 과목리스트 보여줌
 * #수정 버튼을 누르면 다이얼로그를 띄우고 값 가져와서 반영.
 * #(원래 값 들어있어야함)
 * #추가 버튼을 누르면 다이얼로그를 띄우고 값 가져와서 반영.
 * #삭제 버튼을 누르면 정말로 삭제할거냐 묻는 다이얼로그 띄움.
 * #예를 누르면 해당 아이템을 삭제.
 * #컬러픽커로 배경색 커스텀 기능 만듦
 * #과목명, ID, PW, color, alarmbefore, useAlarm를 담은 SQLite 생성
 * #SQLite 연동
 * #앱을 들어올때마다 SQLite에서 데이터를 가져와서 표시해야함.
 * frag_timetable에서 데이터 반영!
 *******************************************************/

public class subject_setting extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    static String SUBJECT = "subject";
    ArrayList<String> arrayIndex = new ArrayList<>();
    Long nowIndex;

    RecyclerView recyclerView;
    subject_rvAdapter recyclerviewAdapter;
    CardView cv_add;

    Dialog dialog1;
    TextView tv_dialogType1;
    EditText et_subject, et_ID, et_PW;
    Button btn_cancel1, btn_next, btn_colorPicker;
    int tempColor;

    Dialog dialog2;
    TextView tv_dialogType2;
    NumberPicker np_alarmTime;
    Button btn_cancel2, btn_ok;
    CheckBox cb_alarm;
    LinearLayout linearLayout;

    Dialog dl_delete;
    Button btn_cancel_delete, btn_ok_delete;

    int ADDITEM = 0;
    int CHANGEITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_setting);

        recyclerView = findViewById(R.id.rv_subject);
        cv_add = findViewById(R.id.cv_add);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerviewAdapter = new subject_rvAdapter();
        recyclerView.setAdapter(recyclerviewAdapter);

        //readDB
        dbOpenHelper = new DBOpenHelper(this);
        dbOpenHelper.open(SUBJECT);
        arrayIndex.clear();

        Cursor cursor = dbOpenHelper.selectColumns(SUBJECT);
        while (cursor.moveToNext()) {
            SubjectData subjectData = new SubjectData();
            String tempIndex = cursor.getString(cursor.getColumnIndex("_id"));
            String tempSubjectName = cursor.getString(cursor.getColumnIndex("subjectName"));
            String tempID = cursor.getString(cursor.getColumnIndex("id"));
            String tempPW = cursor.getString(cursor.getColumnIndex("password"));
            int tempColor = cursor.getInt(cursor.getColumnIndex("color"));
            String tempAlarmTime = cursor.getString(cursor.getColumnIndex("alarm_before"));
            boolean tempUseAlarm = (cursor.getInt(cursor.getColumnIndex("useAlarm")) != 0);
            subjectData.setSubject(tempSubjectName);
            subjectData.setID(tempID);
            subjectData.setPW(tempPW);
            subjectData.setColor(tempColor);
            subjectData.setAlarmBefore(Integer.parseInt(tempAlarmTime));
            subjectData.setUseAlarm(tempUseAlarm);
            recyclerviewAdapter.addItem(subjectData);
            arrayIndex.add(tempIndex);
        }
        cursor.close();

        setDialog();

        final ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        recyclerviewAdapter.setOnItemClickListener(new subject_rvAdapter.OnItemClickListener() {
            @Override
            public void onCopyClick(int position) {
                ClipData clip = ClipData.newPlainText("ID", recyclerviewAdapter.items.get(position).getID());
                clipboard.setPrimaryClip(clip);
                //TODO:ID,PW를 어떤식으로 복붙할건지는 생각해봐야할듯.
            }

            @Override
            public void onChangeClick(int position) {
                SubjectData recentSubjectData = recyclerviewAdapter.items.get(position);
                et_subject.setText(recentSubjectData.getSubject());
                et_ID.setText(recentSubjectData.getID());
                et_PW.setText(recentSubjectData.getPW());
                tempColor = recentSubjectData.getColor();
                btn_colorPicker.setBackgroundColor(tempColor);
                cb_alarm.setChecked(recentSubjectData.getUseAlarm());
                if (recentSubjectData.getUseAlarm()) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.INVISIBLE);
                }
                np_alarmTime.setValue(recentSubjectData.getAlarmBefore());

                runDialog(position, CHANGEITEM);
            }

            @Override
            public void onDeleteClick(final int position) {
                dl_delete.show();

                btn_cancel_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dl_delete.dismiss();
                    }
                });
                btn_ok_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerviewAdapter.items.remove(position);
                        recyclerviewAdapter.notifyDataSetChanged();
                        nowIndex = Long.parseLong(arrayIndex.get(position));
                        dbOpenHelper.deleteColumn(nowIndex, SUBJECT);
                        freshArray();
                        dl_delete.dismiss();
                    }
                });
            }
        });

        cv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempColor = Color.parseColor("#FFFFFF");
                et_subject.setText("");
                et_ID.setText("");
                et_PW.setText("");
                btn_colorPicker.setBackgroundColor(tempColor);
                cb_alarm.setChecked(false);
                np_alarmTime.setValue(0);
                runDialog(recyclerviewAdapter.items.size(), ADDITEM);
            }
        });
        cv_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dbOpenHelper.deleteAllColumns(SUBJECT);
                recyclerviewAdapter.items.clear();
                recyclerviewAdapter.notifyDataSetChanged();
                freshArray();
                return true;
            }
        });
    }

    void freshArray() {
        arrayIndex.clear();
        Cursor cursor = dbOpenHelper.selectColumns(SUBJECT);
        while (cursor.moveToNext()) {
            String tempIndex = cursor.getString(cursor.getColumnIndex("_id"));
            arrayIndex.add(tempIndex);
        }
        cursor.close();
    }

    public void addItem(String subject, String ID, String PW, int color, int alarmTime, Boolean useAlarm) {
        SubjectData subjectData = new SubjectData();
        subjectData.setSubject(subject);
        subjectData.setID(ID);
        subjectData.setPW(PW);
        subjectData.setColor(color);
        subjectData.setAlarmBefore(alarmTime);
        subjectData.setUseAlarm(useAlarm);
        recyclerviewAdapter.addItem(subjectData);
        recyclerviewAdapter.notifyDataSetChanged();
        dbOpenHelper.insertSubject(subjectData);
        freshArray();
    }

    public void changeItem(int position, String subject, String ID, String PW, int color, int alarmTime, Boolean useAlarm) {
        recyclerviewAdapter.items.remove(position);
        SubjectData subjectData = new SubjectData();
        subjectData.setSubject(subject);
        subjectData.setID(ID);
        subjectData.setPW(PW);
        subjectData.setColor(color);
        subjectData.setAlarmBefore(alarmTime);
        subjectData.setUseAlarm(useAlarm);
        recyclerviewAdapter.items.add(position, subjectData);
        recyclerviewAdapter.notifyDataSetChanged();
        nowIndex = Long.parseLong(arrayIndex.get(position));
        dbOpenHelper.updateSubject(nowIndex, subjectData);
        freshArray();
    }

    void runDialog(final int position, final int changeType) {
        dialog1.show();

        if (changeType == ADDITEM) {
            tv_dialogType1.setText(R.string.add_subject);
            tv_dialogType2.setText(R.string.add_subject);
        } else if (changeType == CHANGEITEM) {
            tv_dialogType1.setText(R.string.change_subject);
            tv_dialogType2.setText(R.string.change_subject);
        }
        btn_colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
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
                        boolean useAlarm = false;
                        if (cb_alarm.isChecked()) useAlarm = true;
                        int alarmTime = np_alarmTime.getValue();
                        if (changeType == ADDITEM) {
                            addItem(subject, ID, PW, tempColor,alarmTime, useAlarm);
                        } else if (changeType == CHANGEITEM) {
                            changeItem(position, subject, ID, PW, tempColor,alarmTime, useAlarm);
                        }
                        dialog2.dismiss();
                        dialog1.dismiss();
                    }
                });

            }
        });
    }

    void setDialog() {
        dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.subject_dialog1);
        dialog1.setOwnerActivity(subject_setting.this);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.subject_dialog2);
        dialog2.setOwnerActivity(subject_setting.this);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dl_delete = new Dialog(this);
        dl_delete.setContentView(R.layout.subject_delete_dialog);
        dl_delete.setOwnerActivity(subject_setting.this);
        dl_delete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_dialogType1 = dialog1.findViewById(R.id.tv_dialogType1);
        et_subject = dialog1.findViewById(R.id.et_subject);
        et_ID = dialog1.findViewById(R.id.et_ID);
        et_PW = dialog1.findViewById(R.id.et_PW);
        btn_colorPicker = dialog1.findViewById(R.id.btn_colorPicker);
        btn_cancel1 = dialog1.findViewById(R.id.btn_cancel);
        btn_next = dialog1.findViewById(R.id.btn_dialogNext);

        tv_dialogType2 = dialog2.findViewById(R.id.tv_dialogType2);
        cb_alarm = dialog2.findViewById(R.id.cb_alarm);
        np_alarmTime = dialog2.findViewById(R.id.np_alarmTime);
        btn_cancel2 = dialog2.findViewById(R.id.btn_cancel2);
        btn_ok = dialog2.findViewById(R.id.btn_dialogOk);
        linearLayout = dialog2.findViewById(R.id.linearLDialog);
        np_alarmTime.setMinValue(0);
        np_alarmTime.setMaxValue(20);

        btn_cancel_delete = dl_delete.findViewById(R.id.btn_cancel_delete);
        btn_ok_delete = dl_delete.findViewById(R.id.btn_ok_delete);
    }

    void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();

        colors.add("#ffab91");
        colors.add("#F48FB1");
        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");
        colors.add("#90caf9");
        colors.add("#81d4fa");
        colors.add("#80deea");
        colors.add("#80cbc4");
        colors.add("#c5e1a5");
        colors.add("#e6ee9c");
        colors.add("#fff59d");
        colors.add("#ffe082");
        colors.add("#ffcc80");
        colors.add("#bcaaa4");

        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        tempColor = color;
                        btn_colorPicker.setBackgroundColor(color);
                    }
                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbOpenHelper.close(SUBJECT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbOpenHelper.open(SUBJECT);
    }
}
