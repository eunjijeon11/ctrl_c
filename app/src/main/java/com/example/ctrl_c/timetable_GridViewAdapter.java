package com.example.ctrl_c;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.ToDoubleBiFunction;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class timetable_GridViewAdapter extends RecyclerView.Adapter<timetable_GridViewAdapter.ViewHolder> {

    ArrayList<ArrayList<String>> items;
    //TODO: made Adapter for timetable

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void onBind() {

        }
    }

    public void addItem(ArrayList<String> classes) {
        items.add(classes);
    }
}
