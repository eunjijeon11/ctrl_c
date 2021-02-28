package com.example.ctrl_c;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class timetable_show_rvAdapter extends RecyclerView.Adapter<timetable_show_rvAdapter.ViewHolder> {

    ArrayList<SubjectData> items = new ArrayList<>();
    ArrayList<String> times = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_show_recyclerview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(items.get(position), times.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SubjectData subjectData, String time) {
        items.add(subjectData);
        times.add(time);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subject, tv_id, tv_pw, tv_time;
        CardView cv_background;
        ImageButton btn_copy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_background = itemView.findViewById(R.id.cv_show);
            tv_subject = itemView.findViewById(R.id.tv_show_subject);
            tv_id = itemView.findViewById(R.id.tv_show_id);
            tv_pw = itemView.findViewById(R.id.tv_show_pw);
            tv_time = itemView.findViewById(R.id.tv_show_time);
            btn_copy = itemView.findViewById(R.id.btn_show_copy);

            final ClipboardManager clipboardManager = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            btn_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String copyText =
                            "start at " + times.get(getAdapterPosition())
                            +"\n"
                            + "subject name: " + items.get(getAdapterPosition()).getSubject()
                            + "\n"
                            + "id: " + items.get(getAdapterPosition()).getID()
                            + "\n"
                            + "pw: " + items.get(getAdapterPosition()).getPW();
                    ClipData clip = ClipData.newPlainText("subjectInfo", copyText);
                    clipboardManager.setPrimaryClip(clip);
                }
            });
        }

        public void onBind(SubjectData subjectData, String time) {
            tv_subject.setText(subjectData.getSubject());
            tv_id.setText(subjectData.getID());
            tv_pw.setText(subjectData.getPW());
            tv_time.setText(time);
            cv_background.setCardBackgroundColor(subjectData.getColor());
        }
    }
}
