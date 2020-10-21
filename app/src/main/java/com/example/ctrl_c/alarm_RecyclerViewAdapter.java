package com.example.ctrl_c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class alarm_RecyclerViewAdapter extends RecyclerView.Adapter<alarm_RecyclerViewAdapter.ViewHolder>{

    /***************************************************
     *
     *뷰홀더 안에 뷰에 들어가는 텍스트뷰, 버튼 등등 선언해주기.
     * findViewById 해주기.
     * onBind에서 Data 클라스에서 선언했던 함수사용해서 알람정보 불러오기/표시
     */

    public ArrayList<Data> items = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_alarmName;
        TextView Mon, Tue, Wed, Thu, Fri, Sat, Sun;
        Switch onOff;
        Button btn_change; //알람정보 수정버튼! 눌렀을 때 알람 세부설정 화면으로 넘어가게 만들면 될듯
        //삭제버튼도 만들거면 추가하고!

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //findViewById
            //button setOnClickListener
        }

        void onBind(Data data) {
            tv_alarmName.setText(data.getAlarmName());
            boolean[] day = data.getDay();
            //요일별로 활성화/비활성화를 나타내는 boolean형 배열
            for (int i=0; i<7; i++) {
                if (day[i]) {
                    //요일 활성화되었다는 표시해주기(색을 바꾼다던지...)
                    //Mon.setTextColor();
                }
            }
            onOff.setChecked(data.getOnOff());
        }
    }

    onItemClickListener listener;

    public interface onItemClickListener {
        void onChangeClick(int position); //수정 버튼 클릭했을 때 호출. frag_alarm에서 함수내용 작성해서 사용.
    }

    public void setOnItenClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    //내장함수(리사이클러뷰 기본함수)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_recyclerview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull alarm_RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void addItem(Data data) { //이 함수를 frag_alarm에서 사용하면 돼! 아이템 추가함수야
        items.add(data);
    }
}
