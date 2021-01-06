package com.example.ctrl_c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class timetable_row_rvAdapter extends RecyclerView.Adapter<timetable_row_rvAdapter.ViewHolder> {

    ArrayList<rowData> items = new ArrayList<>();

    private OnItemClickListener listener = null;
    public interface OnItemClickListener {
        public void onItemChange(View v, int position);
        public void onItemDelete(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_row_view, parent, false);
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
        ImageButton btn_change;
        ImageButton btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_row = itemView.findViewById(R.id.tv_timetable_row);
            btn_change = itemView.findViewById(R.id.btn_row_change);
            btn_delete = itemView.findViewById(R.id.btn_row_delete);

            btn_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //열 정보 변환
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            listener.onItemChange(v, position);
                        }
                    }
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //열 삭제
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            listener.onItemDelete(v, position);
                        }
                    }
                }
            });
        }

        public void onBind(rowData rowdata) {
            tv_row.setText(String.valueOf(rowdata.getRow()));
        }
    }
}
