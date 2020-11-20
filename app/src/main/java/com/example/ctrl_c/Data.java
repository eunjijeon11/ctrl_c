package com.example.ctrl_c;

public class Data {

    //subject_recyclerview

    private String subject;
    private String ID;
    private String PW;
    private int alarmBefore;
    private Boolean useAlarm;
    private String color;

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

    public int getAlarmBefore() {
        return alarmBefore;
    }

    public void setAlarmBefore(int alarmBefore) {
        this.alarmBefore = alarmBefore;
    }

    public Boolean getUseAlarm() {
        return useAlarm;
    }

    public void setUseAlarm(Boolean useAlarm) {
        this.useAlarm = useAlarm;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
