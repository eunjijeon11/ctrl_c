package com.example.ctrl_c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/******************************************************
 * TODO: made timetable                      ******DONE
 * TODO: enable timetable setting
 * TODO: save setting in SQLite Database
 *******************************************************/
public class timetable_setting extends AppCompatActivity {

    RecyclerView rv_subjectView;
    timetable_subject_rvAdapter ra_timetable_subject;
    int selectedPosition = -1;
    SubjectData recentData;

    RecyclerView rv_timetable;
    timetable_rvAdapter ra_timetable;
    RecyclerView rv_timetable_row;
    timetable_row_rvAdapter ra_timetable_row;

    GridLayout gl_timetable;
    CardView cv_addRow;

    DBOpenHelper dbOpenHelper;
    String SUBJECT = "subject";
    String TIMETABLE = "timetable";

    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_setting);

        //init
        rv_timetable = findViewById(R.id.rv_timetable);
        rv_timetable_row = findViewById(R.id.rv_timetable_row);
        rv_subjectView = findViewById(R.id.rv_timetable_subject);
        cv_addRow = findViewById(R.id.cv_addRow);
        gl_timetable = findViewById(R.id.gl_timetable);
        btn_save = findViewById(R.id.btn_timetable_save);

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
        rv_timetable_row.setAdapter(ra_timetable_row);

        //시간표 기능
        ra_timetable_row.setOnItemClickListener(new timetable_row_rvAdapter.OnItemClickListener() {
            @Override
            public void onItemChange(View v, int position) {
                //다이얼로그 띄워서 교시 정보 바꾸기
            }

            @Override
            public void onItemDelete(View v, int position) {
                //교시 지우기
                ra_timetable_row.items.remove(position);
                for (int i=0;i<ra_timetable_row.getItemCount();i++) {
                    if (i >= position) {
                        rowData temp = ra_timetable_row.items.get(i);
                        temp.setRow(temp.getRow()-1);
                        ra_timetable_row.items.set(i, temp);
                    }
                }
                ra_timetable_row.notifyDataSetChanged();

                for (int i=0;i<7;i++) {
                    ra_timetable.items.remove(position * 7);
                }
                ra_timetable.notifyDataSetChanged();
            }
        });
        addRow(); //첫번째 줄 추가 or readDB

        //subject recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_subjectView.setLayoutManager(linearLayoutManager);
        ra_timetable_subject = new timetable_subject_rvAdapter();
        rv_subjectView.setAdapter(ra_timetable_subject);

        readDB();

        //과목 버튼 기능
        ra_timetable_subject.setOnItemSelectedListener(new timetable_subject_rvAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(int position) {
                if (position != selectedPosition) { //같은 과목을 클릭한게 아니면:
                    if (selectedPosition > -1) { //첫번째 선택이 아니면 이전 선택 버튼 상태 되돌림
                        recentData.setID("unselected");
                        ra_timetable_subject.items.set(selectedPosition, recentData);
                    }
                    recentData = ra_timetable_subject.items.get(position); //recentdata 바꿈
                    recentData.setID("selected");
                    ra_timetable_subject.items.set(position, recentData);
                    ra_timetable_subject.notifyDataSetChanged();
                    selectedPosition = position;
                }
            }
        });

        cv_addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //시간표 저장
            }
        });
    }

    void addRow() {
        for(int i=0;i<7;i++) {
            SubjectData data = new SubjectData();
            data.setSubject("NONE");
            data.setColor(Color.parseColor("#ffffff"));
            ra_timetable.addItem(data);
        }
        ra_timetable.notifyDataSetChanged();

        rowData rowdata = new rowData();
        rowdata.setRow(ra_timetable_row.getItemCount() + 1);
        //rowdata.setStartTime(); 다이얼로그에서 시작 시간 받아서 넣기
        ra_timetable_row.addItem(rowdata);
        ra_timetable_row.notifyDataSetChanged();
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
                tv_timetable_subject.setText(subjectData.getSubject());
                GradientDrawable drawable = (GradientDrawable) ll_timetable_subject.getBackground();
                String type = subjectData.getID();
                if(type.equals("selected")) { //선택
                    String fillColor =  String.format("#%06X", (0xFFFFFF & subjectData.getColor()));
                    String strokeColor = colors.get(fillColor);
                    drawable.setCornerRadius(50);
                    drawable.setStroke(10, Color.parseColor(strokeColor));
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
