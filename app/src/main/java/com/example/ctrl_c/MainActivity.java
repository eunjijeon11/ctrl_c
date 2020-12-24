package com.example.ctrl_c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    frag_alarm frag_alarm;
    frag_timetable frag_timetable;
    frag_setting frag_setting;

    static String SUBJECT = "subject";
    static String TIMETABLE = "timetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBOpenHelper mdbOpenHelper = new DBOpenHelper(this);
        mdbOpenHelper.open(SUBJECT);
        mdbOpenHelper.create(SUBJECT);
        mdbOpenHelper.close(SUBJECT);

        bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.alarm:
                        setFrag(0);
                        break;
                    case R.id.timetable:
                        setFrag(1);
                        break;
                    case R.id.setting:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });
        frag_alarm = new frag_alarm();
        frag_timetable = new frag_timetable();
        frag_setting = new frag_setting();
        setFrag(0);

    }

    private void setFrag(int n) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (n) {
            case 0:
                fragmentTransaction.replace(R.id.frame_main, frag_alarm);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.frame_main, frag_timetable);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.frame_main, frag_setting);
                fragmentTransaction.commit();
                break;
        }
    }
}
