package com.example.ctrl_c;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class subject_setting extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    static String SUBJECT = "subject";
    ArrayList<String> arrayIndex = new ArrayList<>();
    Long nowIndex;

    RecyclerView recyclerView;
    subject_rvAdapter recyclerviewAdapter;
    FloatingActionButton btn_add;

    Dialog dialog1;
    TextView tv_dialogType1, tv_warning;
    EditText et_subject, et_ID, et_PW;
    Button btn_cancel1, btn_next;
    int tempColor;
    int selectedColor = -1;
    GridLayout gl_colorPick;

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
        btn_add = findViewById(R.id.fab_addSubject);

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

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempColor = Color.parseColor("#FFFFFF");
                et_subject.setText("");
                et_ID.setText("");
                et_PW.setText("");
                cb_alarm.setChecked(false);
                np_alarmTime.setValue(0);
                runDialog(recyclerviewAdapter.items.size(), ADDITEM);
            }
        });
        btn_add.setOnLongClickListener(new View.OnLongClickListener() {
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
        if (selectedColor > -1) {
            LinearLayout tempLinearLayout = (LinearLayout) gl_colorPick.getChildAt(selectedColor);
            GradientDrawable tempDrawable = (GradientDrawable) tempLinearLayout.getBackground();
            tempDrawable.setColor(tempColor);
            tempDrawable.setStroke(0, Color.parseColor("#000000"));
        }
        selectedColor = -1;

        if (changeType == ADDITEM) {
            tv_dialogType1.setText(R.string.add_subject);
            tv_dialogType2.setText(R.string.add_subject);
        } else if (changeType == CHANGEITEM) {
            tv_dialogType1.setText(R.string.change_subject);
            tv_dialogType2.setText(R.string.change_subject);
        }
        tv_warning.setVisibility(View.GONE);
        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recyclerviewAdapter.isSameName(et_subject.getText().toString(), position)) {
                    tv_warning.setVisibility(View.GONE);

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
                } else {
                    tv_warning.setVisibility(View.VISIBLE);
                }
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
        tv_warning = dialog1.findViewById(R.id.tv_warning);
        et_ID = dialog1.findViewById(R.id.et_ID);
        et_PW = dialog1.findViewById(R.id.et_PW);
        btn_cancel1 = dialog1.findViewById(R.id.btn_cancel1);
        btn_next = dialog1.findViewById(R.id.btn_dialogNext);
        gl_colorPick = dialog1.findViewById(R.id.gl_colorPick);

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

        final String[] colors = getResources().getStringArray(R.array.color_light);
        for (int i=0;i<gl_colorPick.getChildCount();i++) {
            final LinearLayout container = (LinearLayout) gl_colorPick.getChildAt(i);
            final GradientDrawable drawable = (GradientDrawable) container.getBackground();
            final int color = Color.parseColor(colors[i]);
            drawable.setColor(color);
            final int finalI = i;
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawable.setStroke(3, Color.parseColor("#000000"));
                    if (selectedColor > -1) {
                        LinearLayout tempLinearLayout = (LinearLayout) gl_colorPick.getChildAt(selectedColor);
                        GradientDrawable tempDrawable = (GradientDrawable) tempLinearLayout.getBackground();
                        tempDrawable.setColor(tempColor);
                        tempDrawable.setStroke(0, Color.parseColor("#000000"));
                    }
                    selectedColor = finalI;
                    tempColor = color;
                }
            });
        }
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
