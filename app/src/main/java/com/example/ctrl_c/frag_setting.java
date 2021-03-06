package com.example.ctrl_c;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class frag_setting extends Fragment {

    View view;
    Button btn_subject, btn_timetable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        btn_subject = view.findViewById(R.id.btn_subject_setting);
        btn_timetable = view.findViewById(R.id.btn_timetable_setting);

        btn_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), subject_setting.class);
                startActivity(intent);
            }
        });
        btn_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), timetable_setting.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
