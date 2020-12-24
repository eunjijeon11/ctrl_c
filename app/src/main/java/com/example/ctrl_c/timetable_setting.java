package com.example.ctrl_c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

/******************************************************
 * TODO: made timetable                      ******DONE
 * TODO: enable timetable setting
 * TODO: save setting in SQLite Database
 *******************************************************/
public class timetable_setting extends AppCompatActivity {

    RecyclerView rv_subjectView;
    timetable_subject_recyclerviewAdapter ra_timetable_subject;

    RecyclerView rv_timetable;
    timetable_recyclerViewAdapter ra_timetable;

    GridLayout gl_timetable;
    CardView cv_addRow;

    DBOpenHelper dbOpenHelper;
    String SUBJECT = "subject";
    String TIMETABLE = "timetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_setting);

        rv_timetable = findViewById(R.id.rv_timetable);
        rv_subjectView = findViewById(R.id.rv_timetable_subject);
        cv_addRow = findViewById(R.id.cv_addRow);
        gl_timetable = findViewById(R.id.gl_timetable);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        rv_timetable.setLayoutManager(gridLayoutManager);
        ra_timetable = new timetable_recyclerViewAdapter();
        rv_timetable.setAdapter(ra_timetable);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_subjectView.setLayoutManager(linearLayoutManager);
        ra_timetable_subject = new timetable_subject_recyclerviewAdapter();
        rv_subjectView.setAdapter(ra_timetable_subject);

        for(int i=0;i<5;i++) {
            SubjectData data = new SubjectData();
            data.setSubject("");
            data.setColor("#ffffff");
            ra_timetable.addItem(data);
        }
        ra_timetable.notifyDataSetChanged();

        cv_addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<5;i++) {
                    SubjectData data = new SubjectData();
                    data.setSubject("");
                    data.setColor("#ffffff");
                    ra_timetable.addItem(data);
                }
                ra_timetable.notifyDataSetChanged();

                //gl_timetable.addView();
            }
        });

        readDB();
    }

    void readDB() {
        dbOpenHelper = new DBOpenHelper(this);
        dbOpenHelper.open(SUBJECT);
        Cursor cursor = dbOpenHelper.selectColumns(SUBJECT);
        while(cursor.moveToNext()) {
            String tempSubject = cursor.getString(cursor.getColumnIndex("subjectName"));
            String tempColor = cursor.getString(cursor.getColumnIndex("color"));
            SubjectData subjectData = new SubjectData();
            subjectData.setSubject(tempSubject);
            subjectData.setColor(tempColor);
            ra_timetable_subject.addItem(subjectData);
        }
        ra_timetable_subject.notifyDataSetChanged();
        dbOpenHelper.close(SUBJECT);
    }

    public static class timetable_subject_recyclerviewAdapter extends RecyclerView.Adapter<timetable_subject_recyclerviewAdapter.ViewHolder>{

        ArrayList<SubjectData> items = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_subject_recyclerview, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.onBind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_timetable_subject;
            CardView cv_timetable_subject;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_timetable_subject = itemView.findViewById(R.id.tv_timetable_subject);
                cv_timetable_subject = itemView.findViewById(R.id.cv_timetable_subject);
            }

            public void onBind(SubjectData subjectData) {
                tv_timetable_subject.setText(subjectData.getSubject());
                cv_timetable_subject.setCardBackgroundColor(Color.parseColor(subjectData.getColor()));
            }
        }

        public void addItem(SubjectData subjectData) {
            items.add(subjectData);
        }
    }
}
