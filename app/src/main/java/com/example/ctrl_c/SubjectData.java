package com.example.ctrl_c;

import java.io.Serializable;

public class SubjectData implements Serializable {

    //subject_recyclerview

    private String subject;
    private String ID;
    private String PW;
    private int color;

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
