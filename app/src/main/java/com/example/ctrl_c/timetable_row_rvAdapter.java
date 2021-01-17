package com.example.ctrl_c;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class timetable_row_rvAdapter extends RecyclerView.Adapter<timetable_row_rvAdapter.ViewHolder> {

    ArrayList<rowData> items = new ArrayList<>();

    private OnItemClickListener listener = null;
    public interface OnItemClickListener {
        void onLongClick(View v, int position);
    }
    public void setOnLongClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_recyclerview, parent, false);
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

    public void addItem(rowData rowdata) {
        items.add(rowdata);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_row;
        CardView cv_row;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_row = itemView.findViewById(R.id.tv_timetable);
            cv_row = itemView.findViewById(R.id.cv_timetable);
            cv_row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }

        public void onBind(rowData rowdata) {
            tv_row.setText(String.valueOf(rowdata.getRow()));
        }
    }
}
