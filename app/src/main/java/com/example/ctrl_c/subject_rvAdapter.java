package com.example.ctrl_c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class subject_rvAdapter extends RecyclerView.Adapter<subject_rvAdapter.ViewHolder> {

    public ArrayList<SubjectData> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCopyClick(int position);
        void onChangeClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subject;
        TextView tv_ID;
        TextView tv_PW;
        ImageButton btn_copy, btn_delete;
        String ID, PW;
        int color;
        CardView cv_background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_ID = itemView.findViewById(R.id.tv_ID);
            tv_PW = itemView.findViewById(R.id.tv_PW);
            btn_copy = itemView.findViewById(R.id.btn_copy);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            cv_background = itemView.findViewById(R.id.cv_subject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onChangeClick(position);
                        }
                    }
                }
            });

            btn_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCopyClick(position);
                        }
                    }
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

        void onbind(SubjectData subjectData) {
            ID = "ID: " + subjectData.getID();
            PW = "PW: " + subjectData.getPW();
            color = subjectData.getColor();

            cv_background.setCardBackgroundColor(color);
            tv_subject.setText(subjectData.getSubject());
            tv_ID.setText(ID);
            tv_PW.setText(PW);
        }
    }

    public boolean isSameName(String name, int position) {
        boolean isSame = false;
        for (int i=0;i<items.size();i++) {
            if (i == position) continue;
            if (items.get(i).getSubject().equals(name)) {
                isSame = true;
            }
        }
        return isSame;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_recyclerview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onbind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean addItem(SubjectData subjectData) {
        items.add(subjectData);
        return true;
    }
}
