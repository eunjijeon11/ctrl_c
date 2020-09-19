package com.example.ctrl_c;

public class Data {

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
}
