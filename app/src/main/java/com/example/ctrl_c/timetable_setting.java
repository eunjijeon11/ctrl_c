package com.example.ctrl_c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/******************************************************
 * TODO: made timetable
 * TODO: enable timetable setting
 * TODO: save setting in sharedPreference
 *******************************************************/
public class timetable_setting extends AppCompatActivity {

    RecyclerView recyclerView;
    timetable_subject_recyclerviewAdapter recyclerviewAdapter;

    DBOpenHelper dbOpenHelper;
    String SUBJECT = "subject";
    String TIMETABLE = "timetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_setting);

        recyclerView = findViewById(R.id.rv_timetable_subject);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerviewAdapter = new timetable_subject_recyclerviewAdapter();
        recyclerView.setAdapter(recyclerviewAdapter);

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
            recyclerviewAdapter.addItem(subjectData);
        }
        recyclerviewAdapter.notifyDataSetChanged();
        dbOpenHelper.close(SUBJECT);

        dbOpenHelper.open(TIMETABLE);
        Cursor cursor1 = dbOpenHelper.selectColumns(TIMETABLE);
        while (cursor.moveToNext()) {
            ArrayList<String> classes = new ArrayList<>();
            classes.add(cursor1.getString(cursor1.getColumnIndex("mon")));
            classes.add(cursor1.getString(cursor1.getColumnIndex("tue")));
            classes.add(cursor1.getString(cursor1.getColumnIndex("wed")));
            classes.add(cursor1.getString(cursor1.getColumnIndex("thu")));
            classes.add(cursor1.getString(cursor1.getColumnIndex("fri")));
            classes.add(cursor1.getString(cursor1.getColumnIndex("sat")));
            classes.add(cursor1.getString(cursor1.getColumnIndex("sun")));
            //gridViewAdapter.addItem(classes);
        }
        //GridViewAdapter.notifyDataDetChanged();
        dbOpenHelper.close(TIMETABLE);
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
