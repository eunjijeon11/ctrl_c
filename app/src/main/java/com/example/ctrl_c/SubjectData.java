package com.example.ctrl_c;

public class SubjectData {

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

    public void setPW(String PW) {
        this.PW = PW;
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

}
