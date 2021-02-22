package com.example.ctrl_c;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class frag_timetable extends Fragment {

    View view;

    RecyclerView recyclerView;
    timetable_show_rvAdapter rvAdapter;

    DBOpenHelper dbOpenHelper;
    HashMap<String, SubjectData> subjects;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timetable, container, false);

        recyclerView = view.findViewById(R.id.rv_timetable_show);

        rvAdapter = new timetable_show_rvAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvAdapter);

        dbOpenHelper = new DBOpenHelper(getActivity());
        dbOpenHelper.open("timetable");
        dbOpenHelper.open("subject");

        subjects = new HashMap<>();
        readSubjects();

        int day = Calendar.DAY_OF_WEEK;
        setClass(day);

        return view;
    }

    void readSubjects() {
        Cursor cursor = dbOpenHelper.selectColumns("subject");
        while (cursor.moveToNext()) {
            String subject = cursor.getString(cursor.getColumnIndex("subjectName"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String pw = cursor.getString(cursor.getColumnIndex("password"));
            int color = cursor.getInt(cursor.getColumnIndex("color"));
            SubjectData subjectData = new SubjectData();
            subjectData.setSubject(subject);
            subjectData.setID(id);
            subjectData.setPW(pw);
            subjectData.setColor(color);

            subjects.put(subject, subjectData);
        }
        cursor.close();
    }

    void setClass(int day) {
        //setClass
        String day_of_week;
        switch (day) {
            case 1: // 일요일
                day_of_week = "sun";
            case 2:
                day_of_week = "mon";
            case 3:
                day_of_week = "tue";
            case 4:
                day_of_week = "wed";
            case 5:
                day_of_week = "thu";
            case 6:
                day_of_week = "fri";
            case 7:
                day_of_week = "sat";
            default:
                day_of_week = "error";
        }

        Cursor cursor = dbOpenHelper.selectColumns("timetable");
        while (cursor.moveToNext()) {
            String subject = cursor.getString(cursor.getColumnIndex(day_of_week));
            int hour = cursor.getInt(cursor.getColumnIndex("hour"));
            int min = cursor.getInt(cursor.getColumnIndex("min"));

            rvAdapter.addItem(subjects.get(subject), hour + ":" + min);
        }
        cursor.close();
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        dbOpenHelper.close("subject");
        dbOpenHelper.close("timetable");
        super.onPause();
    }
}
