package com.example.ctrl_c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/******************************************************
 * TODO: make timetable                      ******DONE
 * TODO: enable timetable setting            ******DONE
 * TODO: save timetable in SQLite Database
 * TODO: make alarm work
 * TODO: row dialog                          ******DONE
 *******************************************************/
public class timetable_setting extends AppCompatActivity {

    RecyclerView rv_subjectView;
    timetable_subject_rvAdapter ra_timetable_subject;
    int oldPosition = -1;
    SubjectData recentData, newData;
    int delete_position = -1;

    RecyclerView rv_timetable;
    timetable_rvAdapter ra_timetable;
    RecyclerView rv_timetable_row;
    timetable_row_rvAdapter ra_timetable_row;

    CardView cv_addRow, cv_save;

    Dialog dialog;
    NumberPicker np_Hour, np_Min;
    SwitchCompat sc_useAlarm;
    Button btn_cancel, btn_finish;
    ImageView iv_delete;

    DBOpenHelper dbOpenHelper;
    String SUBJECT = "subject";
    //String TIMETABLE = "timetable";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_setting);

        //init
        rv_timetable = findViewById(R.id.rv_timetable);
        rv_timetable_row = findViewById(R.id.rv_timetable_row);
        rv_subjectView = findViewById(R.id.rv_timetable_subject);
        cv_addRow = findViewById(R.id.cv_addRow);
        cv_save = findViewById(R.id.cv_timetable_save);
        recentData = null;

        //dialog init
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.timetable_row_dialog);
        dialog.setOwnerActivity(timetable_setting.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        np_Hour = dialog.findViewById(R.id.np_rowHour);
        np_Min = dialog.findViewById(R.id.np_rowMin);
        np_Hour.setMaxValue(23);
        np_Hour.setMinValue(0);
        np_Min.setMaxValue(59);
        np_Min.setMinValue(0);
        sc_useAlarm = new SwitchCompat(this);
        sc_useAlarm = dialog.findViewById(R.id.sc_rowAlarm);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_finish = dialog.findViewById(R.id.btn_finish);
        iv_delete = dialog.findViewById(R.id.iv_delete);

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
        ra_timetable_row.setOnLongClickListener(new timetable_row_rvAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(View v, final int position) {
                Log.e("size", ra_timetable.getItemCount() + "");
                Log.e("row size", ra_timetable_row.getItemCount() + "");
                Log.e("row position", position + "");
                dialog.show();
                np_Hour.setValue(ra_timetable_row.items.get(position).getStartH());
                np_Min.setValue(ra_timetable_row.items.get(position).getStartM());
                sc_useAlarm.setChecked(ra_timetable_row.items.get(position).getUseAlarm());

                final rowData row = ra_timetable_row.items.get(position);
                //dialog setting
                btn_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rowData temp = new rowData();
                        temp.setRow(row.getRow());
                        temp.setStartTime(np_Hour.getValue(), np_Min.getValue());
                        temp.setUseAlarm(sc_useAlarm.isChecked());
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
        newData.setSubject("NONE");
        newData.setColor(Color.parseColor("#00000000"));
        //시간표 클릭이벤트
        ra_timetable.setOnItemClickListener(new timetable_rvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) { //클릭해서 과목정보 수정
                if (delete_position == position) { //삭제버튼 누르면
                    ra_timetable.items.set(position, newData);
                    delete_position = -1;
                    Log.e("is deleted", String.valueOf(true));
                    ra_timetable.notifyDataSetChanged();
                } else if ((delete_position > -1) & (delete_position != position)) { //삭제모드&다른 곳 클릭
                    //원래 뷰로 돌아감
                    delete_position = -1;
                    Log.e("clicked position", String.valueOf(position));
                    Log.e("is deleted", String.valueOf(false));
                    ra_timetable.notifyDataSetChanged();
                } else if ((delete_position == -1) & (recentData != null)) { //삭제모드가 아니고 과목이 선택되어있으면
                    ra_timetable.items.set(position, recentData);
                    Log.e("changed position", String.valueOf(position));
                    ra_timetable.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemLongClick(View v, int position) { //길게 누를 때 한번 호출됨
                Log.e("longClick position", String.valueOf(position));
                //삭제모드로 바뀜
                delete_position = position;
            }

        });

        //subject recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_subjectView.setLayoutManager(linearLayoutManager);
        ra_timetable_subject = new timetable_subject_rvAdapter();
        rv_subjectView.setAdapter(ra_timetable_subject);

        readDB(); //과목 정보 불러오기&시간표 정보 불러오기

        //과목 버튼 기능
        ra_timetable_subject.setOnItemSelectedListener(new timetable_subject_rvAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(int position) {
                if (position != oldPosition) { //같은 과목을 클릭한게 아니면:
                    if (oldPosition > -1) { //첫번째 선택이 아니면 이전 선택 버튼 상태 되돌림
                        recentData.setID("unselected");
                        ra_timetable_subject.items.set(oldPosition, recentData);
                    }
                    recentData = ra_timetable_subject.items.get(position); //recent data 바꿈
                    recentData.setID("selected");
                    ra_timetable_subject.items.set(position, recentData);
                    oldPosition = position;
                } else { //같은 과목 클릭하면 선택 효과 사라짐
                    ra_timetable_subject.items.get(position).setID("unselected");
                    recentData = null;
                    oldPosition = -1;
                }
                ra_timetable_subject.notifyDataSetChanged();
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
                //시간표 저장
            }
        });
    }

    void addRow() {
        dialog.show();
        np_Hour.setValue(0);
        np_Min.setValue(0);
        sc_useAlarm.setChecked(false);

        final rowData temp = new rowData();
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp.setRow(ra_timetable_row.getItemCount() + 1);
                temp.setStartTime(np_Hour.getValue(), np_Min.getValue());
                temp.setUseAlarm(sc_useAlarm.isChecked());
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
        dbOpenHelper.open(SUBJECT);
        Cursor cursor = dbOpenHelper.selectColumns(SUBJECT);
        while(cursor.moveToNext()) {
            String tempSubject = cursor.getString(cursor.getColumnIndex("subjectName"));
            int tempColor = cursor.getInt(cursor.getColumnIndex("color"));
            SubjectData subjectData = new SubjectData();
            subjectData.setSubject(tempSubject);
            subjectData.setColor(tempColor);
            subjectData.setID("unselected");
            ra_timetable_subject.addItem(subjectData);
        }
        ra_timetable_subject.notifyDataSetChanged();
        dbOpenHelper.close(SUBJECT);
    }

    public static class timetable_subject_rvAdapter extends RecyclerView.Adapter<timetable_subject_rvAdapter.ViewHolder>{

        ArrayList<SubjectData> items = new ArrayList<>();

        private static OnItemSelectedListener listener;
        public interface OnItemSelectedListener {
            void onSelected(int position);
        }
        public void setOnItemSelectedListener(OnItemSelectedListener listener) {
            timetable_subject_rvAdapter.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_subject_recyclerview, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String[] DarkColors = holder.itemView.getResources().getStringArray(R.array.color_dark);
            String[] LightColors = holder.itemView.getResources().getStringArray(R.array.color_light);
            Map<String, String> colors = new HashMap<>();
            for (int i=0;i<DarkColors.length;i++) {
                colors.put(LightColors[i], DarkColors[i]);
            }
            holder.onBind(items.get(position), colors);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_timetable_subject;
            LinearLayout ll_timetable_subject;

            public ViewHolder(@NonNull final View itemView) {
                super(itemView);
                tv_timetable_subject = itemView.findViewById(R.id.tv_timetable_subject);
                ll_timetable_subject = itemView.findViewById(R.id.ll_timetable_subject);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onSelected(position);
                        }
                    }
                });
            }

            public void onBind(SubjectData subjectData, Map<String, String> colors) {
                if (subjectData.getSubject().length()>8) {
                    String temp = subjectData.getSubject().substring(0,8).concat("...");
                    tv_timetable_subject.setText(temp);
                } else {
                    tv_timetable_subject.setText(subjectData.getSubject());
                }

                GradientDrawable drawable = (GradientDrawable) ll_timetable_subject.getBackground();
                String type = subjectData.getID();
                if(type.equals("selected")) { //선택
                    drawable.setCornerRadius(60);
                    drawable.setColor(subjectData.getColor());
                    drawable.setStroke(3, Color.parseColor("#000000"));
                } else if (type.equals("unselected")){ //선택 해제
                    drawable.setStroke(0, Color.parseColor("#FFFFFF"));
                    drawable.setColor(subjectData.getColor());
                }
            }
        }

        public void addItem(SubjectData subjectData) {
            items.add(subjectData);
        }
    }
}
