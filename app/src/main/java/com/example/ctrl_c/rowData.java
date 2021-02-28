package com.example.ctrl_c;

//클라스 변수 선언
public class rowData {
    //private int row;
    private int startH;
    private int startM;
    private int alarmBefore;
    private boolean useAlarm;

    public int getStartH() {
        return startH;
    }

    public int getStartM() {
        return startM;
    }

    public void setStartTime(int startH, int startM) {
        this.startH = startH;
        this.startM = startM;
    }

    public int getAlarmBefore() {
        return alarmBefore;
    }

    public void setAlarmBefore(int alarmBefore) {
        this.alarmBefore = alarmBefore;
    }

    public boolean getUseAlarm() {
        return useAlarm;
    }
    public void setUseAlarm(boolean useAlarm) {
        this.useAlarm = useAlarm;
    }
}
