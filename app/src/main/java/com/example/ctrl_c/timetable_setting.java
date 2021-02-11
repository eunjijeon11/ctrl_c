package com.example.ctrl_c;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/******************************************************
 * TODO: make timetable                      ******DONE
 * TODO: enable timetable setting            ******DONE
 * TODO: save timetable in SQLite Database   ******DONE
 * TODO: make alarm work
 * TODO: zoom api
 * TODO: row dialog                          ******DONE
 *******************************************************/
public class timetable_setting extends AppCompatActivity {

    SubjectData recentData, newData;
    int delete_position = -1;

    RecyclerView rv_timetable;
    timetable_rvAdapter ra_timetable;
    RecyclerView rv_timetable_row;
    timetable_row_rvAdapter ra_timetable_row;

    CardView cv_addRow, cv_save;
    ChipGroup chipGroup;
    ArrayList<SubjectData> subjects;

    Dialog dialog;
    NumberPicker np_Hour, np_Min, np_alarmBefore;
    Button btn_cancel, btn_finish;
    ImageView iv_delete;
    SwitchCompat sw_useAlarm;

    DBOpenHelper dbOpenHelper;
    String SUBJECT = "subject";
    String TIMETABLE = "timetable";
    String[] tColumns = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};

    alarmUtil myAlarmUtil;
    ArrayList<Integer> rowList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_setting);

        //init
        rv_timetable = findViewById(R.id.rv_timetable);
        rv_timetable_row = findViewById(R.id.rv_timetable_row);
        cv_addRow = findViewById(R.id.cv_addRow);
        cv_save = findViewById(R.id.cv_timetable_save);
        chipGroup = findViewById(R.id.chip_group);
        recentData = null;
        subjects = new ArrayList<>();

        //dialog init
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.timetable_row_dialog);
        dialog.setOwnerActivity(timetable_setting.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        np_Hour = dialog.findViewById(R.id.np_rowHour);
        np_Min = dialog.findViewById(R.id.np_rowMin);
        np_alarmBefore = dialog.findViewById(R.id.np_alarmBefore);
        np_Hour.setMaxValue(23);
        np_Hour.setMinValue(0);
        np_Min.setMaxValue(59);
        np_Min.setMinValue(0);
        np_alarmBefore.setMaxValue(30);
        np_alarmBefore.setMinValue(0);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_finish = dialog.findViewById(R.id.btn_finish);
        iv_delete = dialog.findViewById(R.id.iv_delete);
        sw_useAlarm = dialog.findViewById(R.id.sw_useAlarm);

        //alarm init
        myAlarmUtil = new alarmUtil(this, "class");

        //timetable recyclerview
        rv_timetable.addItemDecoration(new DividerItemDecoration(this, GridLayoutManager.HORIZONTAL));
        rv_timetable.addItemDecoration(new DividerItemDecoration(this, GridLayoutManager.VERTICAL));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        rv_timetable.setLayoutManager(gridLayoutManager);
        ra_timetable = new timetable_rvAdapter();
        rv_timetable.setAdapter(ra_timetable);

        //timetable row recyclerview
        LinearLayoutManager linearLayoutManager_row = new LinearLayoutManager(this);
        rv_timetable_row.setLayoutManager(linearLayoutManager_row);
        ra_timetable_row = new timetable_row_rvAdapter();
        rv_timetable_row.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv_timetable_row.setAdapter(ra_timetable_row);
        ra_timetable_row.setOnClickListener(new timetable_row_rvAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, final int position) {
                dialog.show();
                np_Hour.setValue(ra_timetable_row.items.get(position).getStartH());
                np_Min.setValue(ra_timetable_row.items.get(position).getStartM());
                np_alarmBefore.setValue(ra_timetable_row.items.get(position).getAlarmBefore());
                sw_useAlarm.setChecked(ra_timetable_row.items.get(position).getUseAlarm());

                final rowData row = ra_timetable_row.items.get(position);
                //dialog setting
                btn_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rowData temp = new rowData();
                        temp.setRow(row.getRow());
                        temp.setStartTime(np_Hour.getValue(), np_Min.getValue());
                        temp.setUseAlarm(sw_useAlarm.isChecked());
                        temp.setAlarmBefore(np_alarmBefore.getValue());
                        ra_timetable_row.items.set(position, temp);
                        ra_timetable_row.notifyDataSetChanged();
                        ra_timetable.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ra_timetable_row.items.remove(position);
                        for (int i=0;i<ra_timetable_row.getItemCount();i++) {
                            if (i >= position) {
                                rowData temp = ra_timetable_row.items.get(i);
                                temp.setRow(temp.getRow()-1);
                                ra_timetable_row.items.set(i, temp);
                            }
                        }

                        for (int i=0;i<7;i++) {
                            ra_timetable.items.remove(position * 7);
                        }
                        ra_timetable_row.notifyDataSetChanged();
                        ra_timetable.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });

        //newData 는 빈칸에 들어가는 데이터이다
        newData = new SubjectData();
        newData.setSubject("none");
        newData.setColor(Color.parseColor("#00000000"));

        //시간표 클릭이벤트
        ra_timetable.setOnItemClickListener(new timetable_rvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) { //클릭해서 과목정보 수정
                if (delete_position == position) { //삭제버튼 누르면
                    ra_timetable.items.set(position, newData);
                    delete_position = -1;
                    ra_timetable.notifyDataSetChanged();
                } else if ((delete_position > -1) & (delete_position != position)) { //삭제모드&다른 곳 클릭
                    //원래 뷰로 돌아감
                    delete_position = -1;
                    ra_timetable.notifyDataSetChanged();
                } else if ((delete_position == -1) & (recentData != null)) { //삭제모드가 아니고 과목이 선택되어있으면
                    ra_timetable.items.set(position, recentData);
                    ra_timetable.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemLongClick(View v, int position) { //길게 누를 때 한번 호출됨
                //삭제모드로 바뀜
                delete_position = position;
            }

        });

        readDB(); //과목 정보 불러오기&시간표 정보 불러오기

        //과목 버튼 기능
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId != -1) {
                    recentData = subjects.get(checkedId-1);
                } else {
                    recentData = null;
                }
            }
        });

        //교시 추가
        cv_addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        });

        cv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbOpenHelper.open(TIMETABLE);
                dbOpenHelper.deleteAllColumns(TIMETABLE); //모두 삭제
                deleteAllAlarm(rowList); //알람 모두 삭제
                rowList = new ArrayList<>();
                for (int row = 0;row<ra_timetable_row.getItemCount();row++) {
                    rowData mData = ra_timetable_row.items.get(row); //교시 정보 얻기
                    Log.e("useAlarm", String.valueOf(mData.getUseAlarm()));
                    ArrayList<SubjectData> classes = new ArrayList<>();
                    for (int i=0;i<7;i++) {
                        SubjectData tempData = ra_timetable.items.get(row * 7 + i);
                        classes.add(tempData);
                    }
                    //알람 설정
                    if (mData.getUseAlarm()) {
                        myAlarmUtil.setClasses(classes);
                        myAlarmUtil.setAlarm(mData.getRow() * 10, mData.getStartH(), mData.getStartM()-mData.getAlarmBefore(), true);
                    } else {
                        myAlarmUtil.setAlarm(mData.getRow() * 10, 0, 0, false);
                    }
                    rowList.add(mData.getRow());
                    //데이터 쓰기
                    dbOpenHelper.insertClasses(classes, mData);
                }
                dbOpenHelper.close(TIMETABLE);

                Toast.makeText(timetable_setting.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void addRow() {
        dialog.show();
        np_Hour.setValue(0);
        np_Min.setValue(0);
        np_alarmBefore.setValue(0);
        sw_useAlarm.setChecked(false);

        final rowData temp = new rowData();
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp.setRow(ra_timetable_row.getItemCount() + 1);
                temp.setStartTime(np_Hour.getValue(), np_Min.getValue());
                temp.setUseAlarm(sw_useAlarm.isChecked());
                temp.setAlarmBefore(np_alarmBefore.getValue());
                ra_timetable_row.addItem(temp);
                ra_timetable_row.notifyDataSetChanged();

                for(int i=0;i<7;i++) {
                    ra_timetable.addItem(newData);
                }
                ra_timetable.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    void readDB() {
        dbOpenHelper = new DBOpenHelper(this);

        //과목 목록 불러오기
        dbOpenHelper.open(SUBJECT);
        Cursor cursor = dbOpenHelper.selectColumns(SUBJECT);
        while(cursor.moveToNext()) {
            String tempSubject = cursor.getString(cursor.getColumnIndex("subjectName"));
            int tempColor = cursor.getInt(cursor.getColumnIndex("color"));
            String tempId = cursor.getString(cursor.getColumnIndex("id"));
            String tempPW = cursor.getString(cursor.getColumnIndex("password"));
            SubjectData subjectData = new SubjectData();
            subjectData.setSubject(tempSubject);
            subjectData.setColor(tempColor);
            subjectData.setID(tempId);
            subjectData.setPW(tempPW);
            subjects.add(subjectData);

            Chip chip = new Chip(this);
            chip.setText(tempSubject);
            int[][] states = new int[][] {
                    new int[] {android.R.attr.state_activated}
            };
            int[] color = new int[] {tempColor};
            ColorStateList colorStateList = new ColorStateList(states, color);
            chip.setChipBackgroundColor(colorStateList);
            chip.setCheckable(true);
            chip.setActivated(true);
            chipGroup.addView(chip);
        }
        cursor.close();
        dbOpenHelper.close(SUBJECT);

        //시간표 불러오기
        dbOpenHelper.open(TIMETABLE);
        int row = 0;
        rowList = new ArrayList<>();
        Cursor tCursor = dbOpenHelper.selectColumns(TIMETABLE);
        while (tCursor.moveToNext()) { //끝날때까지 반복

            //id 추가
            int tempId = tCursor.getInt(tCursor.getColumnIndex("_id"));

            //rowData 설정
            rowData tempData = new rowData();
            tempData.setStartTime(tCursor.getInt(tCursor.getColumnIndex("hour")), tCursor.getInt(tCursor.getColumnIndex("min")));
            tempData.setRow(row + 1);
            tempData.setUseAlarm(tCursor.getInt(tCursor.getColumnIndex("useAlarm")) == 1);
            tempData.setAlarmBefore(tCursor.getInt(tCursor.getColumnIndex("alarmBefore")));
            ra_timetable_row.items.add(tempData); //row 추가

            rowList.add(row + 1);

            //수업 불러오기
            String[] classes = {"none", "none", "none", "none", "none", "none", "none"};
            for (int i=0;i<7;i++) { //월요일부터 일요일까지
                String tempClass = tCursor.getString(tCursor.getColumnIndex(tColumns[i])); //수업 이름
                int position = isInData(tempClass);
                if (position != -1) { //수업이 과목 정보에 존재하면
                    ra_timetable.addItem(subjects.get(position));
                    classes[i] = tempClass;
                } else {
                    ra_timetable.addItem(newData);
                }
            }
            ra_timetable.notifyDataSetChanged();
            dbOpenHelper.updateClasses(tempId, classes, tempData);
            row++;
        }
        tCursor.close();
        dbOpenHelper.close(TIMETABLE);
    }

    void deleteAllAlarm(ArrayList<Integer> rowList) {
        for (int i=0;i<rowList.size();i++) {
            myAlarmUtil.cancelAlarm(rowList.get(i) * 10);
        }
    }

    int isInData(String tempClass) {
        int isIn = -1;
        for (int i=0;i<subjects.size();i++) {
            if (tempClass.equals(subjects.get(i).getSubject())) isIn = i;
        }
        return isIn;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ra_timetable.notifyDataSetChanged();
        ra_timetable_row.notifyDataSetChanged();
    }
}
