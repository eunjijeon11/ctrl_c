package com.example.ctrl_c;

public class Data {

    //subject_recyclerview

    private String subject;
    private String ID;
    private String PW;
    private int alarmTime;
    private Boolean useAlarm;

    public  String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String M) {
        this.PW = M;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Boolean getUseAlarm() {
        return useAlarm;
    }

    public void setUseAlarm(Boolean useAlarm) {
        this.useAlarm = useAlarm;
    }

    //alarm_recyclerview

    private String alarmName;
    private boolean[] day;
    private boolean onOff;

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public boolean[] getDay() {
        return day;
    }

    public void setDk(boolean[] day) {
        this.day = day;
    }

    public boolean getOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }
}
